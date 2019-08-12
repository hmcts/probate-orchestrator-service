package uk.gov.hmcts.probate.client.business;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    List<String> uploadDocument(
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
