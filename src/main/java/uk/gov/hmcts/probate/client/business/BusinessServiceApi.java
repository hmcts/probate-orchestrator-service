package uk.gov.hmcts.probate.client.business;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.probate.model.PhonePin;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.DocumentNotification;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.ExecutorNotification;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@FeignClient(
        name = "business-service-api",
        url = "${business.service.api.url}",
        configuration = BusinessServiceConfiguration.class
)
public interface BusinessServiceApi {

    @PostMapping(
            value = "/businessDocument/generateCheckAnswersSummaryPDF",
            headers = {
                CONTENT_TYPE + "=" + APPLICATION_JSON_UTF8_VALUE,
                ACCEPT + "=" + APPLICATION_JSON_UTF8_VALUE
            }
    )
    byte[] generateCheckAnswersSummaryPdf(
        @RequestHeader(AUTHORIZATION) String authorization,
        @RequestHeader(BusinessServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody CheckAnswersSummary checkAnswersSummary
    );

    @PostMapping(
            value = "/businessDocument/generateLegalDeclarationPDF",
            headers = {
                CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE,
                ACCEPT + "=" + APPLICATION_OCTET_STREAM_VALUE
            }
    )
    byte[] generateLegalDeclarationPDF(
        @RequestHeader(AUTHORIZATION) String authorization,
        @RequestHeader(BusinessServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody LegalDeclaration legalDeclaration
    );

    @PostMapping(
            value = "/businessDocument/generateBulkScanCoverSheetPDF",
            headers = {
                CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE,
                ACCEPT + "=" + APPLICATION_OCTET_STREAM_VALUE
            }
    )
    byte[] generateBulkScanCoverSheetPDF(
        @RequestHeader(AUTHORIZATION) String authorization,
        @RequestHeader(BusinessServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody BulkScanCoverSheet bulkScanCoverSheet
    );

    @PostMapping(
            value = "/invite",
            headers = {
                CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
            }
    )
    String invite(@RequestBody Invitation invitation,
                         @RequestHeader("Session-Id") String sessionId);


    @PostMapping(path = "/invite/{inviteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    String invite(@PathVariable("inviteId") String inviteId,
                         @RequestBody Invitation invitation,
                         @RequestHeader("Session-Id") String sessionId);

    @PostMapping(path = "/pin", consumes = MediaType.APPLICATION_JSON_VALUE)
    String pinNumberPost(
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody PhonePin phonePin);

    @PostMapping(
            value = "/invite/bilingual",
            headers = {
                CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
            }
    )
    String inviteBilingual(@RequestBody Invitation invitation,
                  @RequestHeader("Session-Id") String sessionId);


    @PostMapping(path = "/invite/bilingual/{inviteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    String inviteBilingual(@PathVariable("inviteId") String inviteId,
                  @RequestBody Invitation invitation,
                  @RequestHeader("Session-Id") String sessionId);

    @PostMapping(path = "/executor-notification/bilingual", consumes = MediaType.APPLICATION_JSON_VALUE)
    String signedBilingual(@RequestBody ExecutorNotification executorNotification);

    @PostMapping(path = "/executor-notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    String signedExec(@RequestBody ExecutorNotification executorNotification);

    @PostMapping(path = "/executor-notification/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    String signedExecAll(@RequestBody ExecutorNotification executorNotification);

    @PostMapping(path = "/executor-notification/all-bilingual", consumes = MediaType.APPLICATION_JSON_VALUE)
    String signedExecAllBilingual(@RequestBody ExecutorNotification executorNotification);

    @PostMapping(path = "/document-upload-notification/bilingual", consumes = MediaType.APPLICATION_JSON_VALUE)
    String documentUploadBilingual(@RequestBody DocumentNotification executorNotification);

    @PostMapping(path = "/document-upload-notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    String documentUpload(@RequestBody DocumentNotification executorNotification);

    @PostMapping(path = "/document-upload-issue-notification/bilingual", consumes = MediaType.APPLICATION_JSON_VALUE)
    String documentUploadIssueBilingual(@RequestBody DocumentNotification executorNotification);

    @PostMapping(path = "/document-upload-issue-notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    String documentUploadIssue(@RequestBody DocumentNotification executorNotification);

    @PostMapping(path = "/pin/bilingual", consumes = MediaType.APPLICATION_JSON_VALUE)
    String pinNumberBilingualPost(
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody PhonePin phonePin);

}
