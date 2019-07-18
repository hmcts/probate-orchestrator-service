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
        //FormDataResource formDatas = persistenceServiceApi.getFormDataByAfterCreateDate(sixMonthsAgo);
        FormDataResource formDatas = persistenceServiceApi.getFormDatas();
        long totalPages = formDatas.getPageMetadata().getTotalPages();
        log.info("Returned from persistence call with " + totalPages + " pages");
        System.out.println("Total Pages: " + totalPages);
        long size = formDatas.getPageMetadata().getSize();
        IntStream.range(0, (int) totalPages).forEach(idx -> {
            int pageNo = idx +1;
            log.info("Getting form data for page " + (pageNo));
            FormDataResource formDataSet = persistenceServiceApi.getFormDataWithPageAndSize(
                   Integer.toString(pageNo), Long.toString(size));
            Collection<FormHolder> formHolders = formDataSet.getContent().getFormdata();
            formHolders.forEach(f -> {
                processFormData(f);
            });
        });
        log.info("Finished Migrating formdata");
    }

    private void processFormData(FormHolder f) {
        LegacyForm formdata = f.getFormdata();
        if (formdata != null) {
            log.info("Processing form data for  " + formdata.getApplicantEmail());
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
            log.info("Check if case created in ccd for formdata with applicantEmail: " + formdata.getApplicantEmail());
            if (formdata.getApplicantEmail() != null && !formdata.getApplicantEmail().isEmpty()) {
                ProbateCaseDetails pcd = submitServiceApi.getCase(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(), formdata.getApplicantEmail(),
                        caseTypeName);
                log.info("Case found for formdata applicant email :  " + formdata.getApplicantEmail());
            }
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                submitServiceApi.saveDraft(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(),
                        formdata.getApplicantEmail(),
                        ProbateCaseDetails.builder().caseData(grantOfRepresentationData).build());
                log.info("Draft Case saved for formdata applicant email :  " + formdata.getApplicantEmail());
            } else {
                log.error("Error with Status code: " + apiClientException.getStatus() + " and error response "
                        + apiClientException.getMessage());
            }
        }
    }
}