package uk.gov.hmcts.probate.core.service.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.model.persistence.InviteDataResource;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InviteData;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class InviteDataMigrator {

    private final PersistenceServiceApi persistenceServiceApi;
    private final SubmitServiceApi submitServiceApi;
    private final SecurityUtils securityUtils;

    public void migrateInviteData() {

        log.info("In migrateFormData!");
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        FormDataResource formDatas = persistenceServiceApi.getFormDataByAfterCreateDate(sixMonthsAgo);


        InviteDataResource inviteDataResource = persistenceServiceApi.getInviteDataByAfterCreateDate(sixMonthsAgo);
        long totalPages = inviteDataResource.getPageMetadata().getTotalPages();
        long size = inviteDataResource.getPageMetadata().getSize();
        log.info("Returned from persistence call with " + totalPages + " pages");
        log.info("Total Pages: {}", totalPages);


        IntStream.range(0, (int) totalPages).forEach(idx -> {
            int pageNo = idx;
            InviteDataResource inviteDataSet =null;
            try {
                log.info("Getting invite data for page " + (pageNo));
                 inviteDataSet = persistenceServiceApi.getInviteDataByAfterCreateDate(sixMonthsAgo,
                        Integer.toString(pageNo), Long.toString(size));
            } catch (RuntimeException e) {
                e.printStackTrace();
                log.info("Could not process invitedata for page {} so skipping!", (pageNo));
                return;
            }
            Collection<InviteData> inviteDatas = inviteDataSet.getContent().getInvitedata();
            inviteDatas.forEach(this::processInviteData);

        });
        log.info("Finished Migrating invite data");
    }

    private void processInviteData(InviteData inviteData) {
        try {
            log.info("Migration started for InviteData with formDataId: {}", inviteData.getFormdataId());
            ProbateCaseDetails pcd = submitServiceApi.getCaseByApplicantEmail(securityUtils.getAuthorisation(),
                    securityUtils.getServiceAuthorisation(), inviteData.getFormdataId(),
                    ProbateType.PA.getCaseType().name());
            if (pcd.getCaseInfo().getState().equals(CaseState.DRAFT)) {
                log.info("Draft case found for formDataId: {}", inviteData.getFormdataId());
                GrantOfRepresentationData grantOfRepresentationData =
                        (GrantOfRepresentationData) pcd.getCaseData();
                log.info("Find executor for for inviteEmail: {} inviteId: {} inviteAgreed: {}", inviteData.getEmail(),
                        inviteData.getId(), inviteData.getAgreed());
                ExecutorApplying e = grantOfRepresentationData.getExecutorApplyingByInviteId(inviteData.getId());
                if (e != null) {
                    updateExecutorWIthInviteDataOnCase(inviteData, pcd, e);
                } else {
                    log.info("No executor applying found on case with inviteId: {}", inviteData.getId());
                }

            }
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                log.info("No case found for invite data with form data id: " + inviteData.getFormdataId());
            } else {
                log.error("Error with Status code: " + apiClientException.getStatus() + " and error response "
                        + apiClientException.getMessage());
            }
        }
    }

    private void updateExecutorWIthInviteDataOnCase(InviteData inviteData, ProbateCaseDetails pcd, ExecutorApplying e) {
        e.setApplyingExecutorInvitationId(inviteData.getId());
        e.setApplyingExecutorAgreed(inviteData.getAgreed());
        submitServiceApi.updateCaseAsCaseWorker(securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(), pcd.getCaseInfo().getCaseId(), pcd);
        log.info("Invite details migrated for formdataId:{} and inviteId:{} and agreed flag:{}",
                inviteData.getFormdataId(), inviteData.getId(), inviteData.getAgreed());
    }
}
