package uk.gov.hmcts.probate.client.business;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.util.List;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(
        name = "business-service-documents-api",
        url = "${business.service.api.url}",
        configuration = BusinessServiceDocumentsConfiguration.class
)
public interface BusinessServiceDocumentsApi {

    @PostMapping(
        value = "/document/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    List<String>  uploadDocument(
        @RequestHeader("user-id") String userID,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestPart("file") MultipartFile file
    );

    @DeleteMapping(value = "/document/delete/{documentId}")
    String delete(
        @RequestHeader("user-id") String userID,
        @PathVariable("documentId") String documentId
    );
}
