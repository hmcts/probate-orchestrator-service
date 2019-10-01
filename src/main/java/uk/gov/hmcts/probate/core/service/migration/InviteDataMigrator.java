package uk.gov.hmcts.probate.core.service.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.model.persistence.InviteDataResource;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InviteData;

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

        InviteDataResource inviteDataResource = persistenceServiceApi.getInviteDatas();
        long totalPages = inviteDataResource.getPageMetadata().getTotalPages();
        long size = inviteDataResource.getPageMetadata().getSize();
        log.info("Total Pages: " + totalPages);
        IntStream.range(0, (int) totalPages).forEach(idx -> {
            int pageNo = idx +1;
            InviteDataResource inviteDataSet = persistenceServiceApi.getInviteDataWithPageAndSize(
                    Integer.toString(pageNo), Long.toString(size));
            Collection<InviteData> inviteDatas = inviteDataSet.getContent().getInvitedata();
            inviteDatas.forEach(this::processInviteData);
        });
        log.info("Finished Migrating formdata");
    }

    private void processInviteData(InviteData inviteData) {
        try {
            log.info("Migration started for InviteData with formDataId: "
                    + inviteData.getFormdataId());
            ProbateCaseDetails pcd = submitServiceApi.getCaseByApplicantEmail(securityUtils.getAuthorisation(),
                    securityUtils.getServiceAuthorisation(), inviteData.getFormdataId(),
                    ProbateType.PA.getCaseType().getName());
            if (pcd.getCaseInfo().getState().equals(CaseState.DRAFT)) {
                log.info("Draft case found for formDataId");
                GrantOfRepresentationData grantOfRepresentationData =
                        (GrantOfRepresentationData) pcd.getCaseData();
                grantOfRepresentationData.setInvitationDetailsForExecutorApplying(inviteData.getEmail(),
                        inviteData.getId(), inviteData.getAgreed());
                submitServiceApi.saveCase(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(),  pcd.getCaseInfo().getCaseId(), pcd);
                log.info("Invite details migrated for formdata id: "
                        + inviteData.getFormdataId());
            }
        } catch (ApiClientException apiClientException) {
            if (apiClientException.getStatus() == HttpStatus.NOT_FOUND.value()) {
                log.info("No case found for invite data with form data id: " + inviteData.getFormdataId());
            } else {
                log.error("ApiClientException thrown for InviteData: " + inviteData.getId());
                throw apiClientException;
            }
        }
    }
}
