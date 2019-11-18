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
import uk.gov.hmcts.probate.model.persistence.InviteDataResource;
import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    private final IdamUsersCsvLoader csvLoader;
    private List<IdamUserEmail> idamUserEmailList;


    public synchronized void migrateFormData() {
        log.info("In migrateFormData!");
        securityUtils.setSecurityContextUserAsCaseworker();
//        System.setProperty("http.proxyHost", "proxyout.reform.hmcts.net");
//        System.setProperty("http.proxyPort", "8080");
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        log.info("Get formData with createDate > " + sixMonthsAgo);
        FormDataResource formDatas = persistenceServiceApi.getFormDataByAfterCreateDate(sixMonthsAgo);
        long totalPages = formDatas.getPageMetadata().getTotalPages();
        long size = formDatas.getPageMetadata().getSize();
        log.info("Returned from persistence call with {} total pages and size {} ", totalPages, size);

        idamUserEmailList = csvLoader.loadIdamUserList("idam_ids.csv");
        List<String> processedCases = new ArrayList<>();
        IntStream.range(0, (int) totalPages).forEach(idx -> {
            int pageNo = idx;
            log.info("Getting form data for page {}", (pageNo));
            FormDataResource formDataSet = null;
            try {
                formDataSet = persistenceServiceApi.getPagedFormDataByAfterCreateDate(sixMonthsAgo,
                        Integer.toString(pageNo), Long.toString(size));
            } catch (RuntimeException e) {
                log.error("Oops!", e);
                log.info("Could not process formdatas for page {} so skipping!", (pageNo));
                return;
            }
            Collection<FormHolder> formHolders = formDataSet.getContent().getFormdata();
            formHolders.forEach(formHolder -> {
                if (!processedCases.contains(formHolder.getFormdata().getApplicantEmail())) {
                    try {
                        processFormData(formHolder, idamUserEmailList);
                        processedCases.add(formHolder.getFormdata().getApplicantEmail());
                    } catch (InterruptedException ie) {
                        log.error("Thread execption!", ie);
                    }
                }
            });
            log.info("Completed formdata migration for page {}", (pageNo));

        });
        log.info("Finished Migrating formdata");
        log.info("Processed {} casedatas : {}", processedCases.size(), processedCases);
    }

    private void processFormData(FormHolder f, List<IdamUserEmail> idamUserEmailList) throws InterruptedException {
        LegacyForm formdata = f.getFormdata();
        Thread.sleep(1000);
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
                                               String caseTypeName) throws InterruptedException {
        try {
            log.info("Check if case created in ccd for formdata with applicantEmail: {}", formdata.getApplicantEmail());
            if (formdata.getCcdCase() == null || formdata.getCcdCase().getId() == null) {
                if (formdata.getApplicantEmail() != null && !formdata.getApplicantEmail().isEmpty()) {
                    submitServiceApi.getCaseByApplicantEmail(securityUtils.getAuthorisation(),
                            securityUtils.getServiceAuthorisation(), formdata.getApplicantEmail(),
                            caseTypeName);
                    log.info("Case found for formdata applicant email:  {}", formdata.getApplicantEmail());
                }
            }
            else{
                log.info("Case already created in CCD with caseId: {} and state: {}", formdata.getCcdCase().getId(), formdata.getCcdCase().getState());
            }
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                saveCaseInCcd(formdata, grantOfRepresentationData);
            } else {
                log.info("Error getting formdata applicant email: {}  with Status code: {} and error message: {}", formdata.getApplicantEmail(), apiClientException.getStatus(), apiClientException.getMessage());
                apiClientException.printStackTrace();
            }
        }
    }

    private void saveCaseInCcd(LegacyForm formdata, GrantOfRepresentationData grantOfRepresentationData) throws InterruptedException {
        setInviteDataForCase(formdata, grantOfRepresentationData);
        ProbateCaseDetails pcd = submitServiceApi.initiateCaseAsCaseWorker(securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                ProbateCaseDetails.builder().caseData(grantOfRepresentationData).build());
        if (pcd != null) {
            log.info("Draft Case saved for formdata applicant email: {}", formdata.getApplicantEmail());
            Optional<IdamUserEmail> userIdFromEmailAddress = getUserIdFromEmailAddress(formdata.getApplicantEmail());
            if (userIdFromEmailAddress.isPresent()) {
                IdamUserEmail idamUserEmail = userIdFromEmailAddress.get();
                submitServiceApi.grantCaseAccessToUserAsCaseWorker(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(), pcd.getCaseInfo().getCaseId(), idamUserEmail.getIdamId().trim());
                log.info("Granted access as caseworker for case id :{} and applicantEmail :{} and userId: {}", pcd.getCaseInfo().getCaseId(), formdata.getApplicantEmail(), idamUserEmail.getIdamId());
            } else {
                log.info("Could not find UserID for: {}", formdata.getApplicantEmail());
            }

        } else {
            log.info("Could not save case for formdata applicant email: {}", formdata.getApplicantEmail());
        }
    }

    private void setInviteDataForCase(LegacyForm formdata, GrantOfRepresentationData grantOfRepresentationData) {
        InviteDataResource inviteDataResource = persistenceServiceApi.getInviteDataByFormDataId(formdata.getApplicantEmail());

        if (inviteDataResource != null && !inviteDataResource.getContent().getInvitedata().isEmpty()) {
            inviteDataResource.getContent().getInvitedata().stream().forEach(inviteData -> {
                log.info("Find executor for for inviteEmail: {} inviteId: {} inviteAgreed: {}", inviteData.getEmail(),
                        inviteData.getId(), inviteData.getAgreed());
                ExecutorApplying e = grantOfRepresentationData.getExecutorApplyingByInviteId(inviteData.getId());
                if (e != null) {
                    e.setApplyingExecutorInvitationId(inviteData.getId());
                    e.setApplyingExecutorAgreed(inviteData.getAgreed());
                    log.info("Invite details set for formdataId:{} and inviteId:{} and agreed flag:{}",
                            inviteData.getFormdataId(), inviteData.getId(), inviteData.getAgreed());
                } else {
                    log.info("ExecutorApplying not found for formdataId:{} and inviteId:{}", inviteData.getFormdataId(), inviteData.getId());
                }

            });
        }
    }

    private Optional<IdamUserEmail> getUserIdFromEmailAddress(String emailAddress) {
        log.info("Get IDAM userId for email: {}", emailAddress);
        return this.idamUserEmailList.stream().filter(idamUserEmail -> idamUserEmail.getEmailAddress().trim().equalsIgnoreCase(emailAddress)).findFirst();
    }
}
