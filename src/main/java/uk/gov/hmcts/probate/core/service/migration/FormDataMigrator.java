package uk.gov.hmcts.probate.core.service.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyIntestacyMapper;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyPaMapper;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.model.persistence.FormHolder;
import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class FormDataMigrator {

    private final PersistenceServiceApi persistenceServiceApi;
    private final SubmitServiceApi submitServiceApi;
    private final SecurityUtils securityUtils;
    private final LegacyPaMapper legacyPaMapper;
    private final LegacyIntestacyMapper legacyIntestacyMapper;

    public void migrateFormData() {

        log.info("In migrateFormData!");
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        log.info("Get formData with createDate > " + sixMonthsAgo);
        FormDataResource formDatas = persistenceServiceApi.getFormDataByAfterCreateDate(sixMonthsAgo);
        long totalPages = formDatas.getPageMetadata().getTotalPages();
        long size = formDatas.getPageMetadata().getSize();
        log.info("Returned from persistence call with {} total pages and size {} ", totalPages, size);
        IntStream.range(0, (int) totalPages).forEach(idx -> {
            int pageNo = idx;
            log.info("Getting form data for page {}", (pageNo));
            FormDataResource formDataSet = null;
            try {
                 formDataSet = persistenceServiceApi.getPagedFormDataByAfterCreateDate(sixMonthsAgo,
                        Integer.toString(pageNo), Long.toString(size));
            }
            catch (RuntimeException e){
                log.error("Oops!", e);
                log.info("Could not process formdatas for page {} so skipping!",  (pageNo));
                return;
            }
            Collection<FormHolder> formHolders = formDataSet.getContent().getFormdata();
            formHolders.forEach(this::processFormData);
            log.info("Completed formdata migration for page {}", (pageNo));

        });
        log.info("Finished Migrating formdata");
    }

    private void processFormData(FormHolder f) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LegacyForm formdata = f.getFormdata();
            if (formdata != null) {
                log.info("Processing form data for {} ", formdata.getApplicantEmail());
                GrantOfRepresentationData grantOfRepresentationData = null;
                if (formdata.getCaseType() != null && formdata.getCaseType().getName().equals("intestacy")) {
                    grantOfRepresentationData = legacyIntestacyMapper.toCaseData(formdata);
                    log.info("Intestacy gop returned");
                    saveDraftCaseIfOneDoesntExist(formdata, grantOfRepresentationData,
                            ProbateType.INTESTACY.getCaseType().name());
                } else {
                    grantOfRepresentationData = legacyPaMapper.toCaseData(formdata);
                    log.info("PA gop returned");
                    saveDraftCaseIfOneDoesntExist(formdata, grantOfRepresentationData,
                            ProbateType.PA.getCaseType().name());
                }
            }

    }

    private void saveDraftCaseIfOneDoesntExist(LegacyForm formdata, GrantOfRepresentationData grantOfRepresentationData,
                                               String caseTypeName) {
        try {
            log.info("Check if case created in ccd for formdata with applicantEmail: {}", formdata.getApplicantEmail());
            if (formdata.getApplicantEmail() != null && !formdata.getApplicantEmail().isEmpty()) {
                submitServiceApi.getCaseByApplicantEmail(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(), formdata.getApplicantEmail(),
                        caseTypeName);
                log.info("Case found for formdata applicant email:  {}", formdata.getApplicantEmail());
            }
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                ProbateCaseDetails pcd = submitServiceApi.initiateCaseAsCaseWorker(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(),
                        ProbateCaseDetails.builder().caseData(grantOfRepresentationData).build());
                if(pcd!=null) {
                    log.info("Draft Case saved for formdata applicant email: {}", formdata.getApplicantEmail());
                    submitServiceApi.grantCaseAccessToUserAsCaseWorker(securityUtils.getAuthorisation(),
                            securityUtils.getServiceAuthorisation(),pcd.getCaseInfo().getCaseId(), formdata.getApplicantEmail());
                    log.info("Granted access as caseworker for case id :{} and applicantEmail :{}",pcd.getCaseInfo().getCaseId(), formdata.getApplicantEmail());
                }
                else{
                    log.info("Could not save case for formdata applicant email: {}", formdata.getApplicantEmail());
                }
            } else {
                log.info("Error getting formdata applicant email: {}  with Status code: {} and error message: {}" , formdata.getApplicantEmail(), apiClientException.getStatus() , apiClientException.getMessage());
                apiClientException.printStackTrace();
            }
        }
    }
}