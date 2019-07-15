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

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        FormDataResource formDatas = persistenceServiceApi.getFormDataByAfterCreateDate(sixMonthsAgo);
        long totalPages = formDatas.getPageMetadata().getTotalPages();
        long size = formDatas.getPageMetadata().getSize();
        for (int i = 0; i < totalPages; i++) {
            FormDataResource formDataSet = persistenceServiceApi.getPagedFormDataByAfterCreateDate(sixMonthsAgo,
                    Integer.toString(i + 1), Long.toString(size));
            Collection<FormHolder> formHolders = formDataSet.getContent().getFormdata();
            formHolders.forEach(f -> {
                LegacyForm formdata = f.getFormdata();
                GrantOfRepresentationData grantOfRepresentationData = null;
                if (formdata.getCaseType() != null && formdata.getCaseType().getName().equals("intestacy")) {
                    grantOfRepresentationData = legacyIntestacyMapper.toCaseData(formdata);
                    saveDraftCaseIfOneDoesntExist(formdata, grantOfRepresentationData,
                            ProbateType.INTESTACY.getCaseType().getName());
                } else {
                    grantOfRepresentationData = legacyPaMapper.toCaseData(formdata);
                    saveDraftCaseIfOneDoesntExist(formdata, grantOfRepresentationData,
                            ProbateType.PA.getCaseType().getName());
                }
            });
        }
    }

    private void saveDraftCaseIfOneDoesntExist(LegacyForm formdata, GrantOfRepresentationData grantOfRepresentationData,
                                               String caseTypeName) {
        try {
            ProbateCaseDetails pcd = submitServiceApi.getCase(securityUtils.getAuthorisation(),
                    securityUtils.getServiceAuthorisation(), formdata.getApplicantEmail(),
                    caseTypeName);
            log.info("Case found for formdata applicant email :  " + formdata.getApplicantEmail());
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                submitServiceApi.saveDraft(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(),
                        formdata.getApplicantEmail(),
                        ProbateCaseDetails.builder().caseData(grantOfRepresentationData).build());
                log.info("Draft Case saved for formdata applicant email :  " + formdata.getApplicantEmail());
            } else {
                log.error(apiClientException.getMessage());
            }
        }
    }
}