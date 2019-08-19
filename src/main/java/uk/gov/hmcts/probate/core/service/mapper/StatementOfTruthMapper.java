package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromDocumentLink;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToDocumentLink;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;

@Component
public class StatementOfTruthMapper {

    private static final String BINARY_URL_SUFFIX = "binary";

    @ToDocumentLink
    public DocumentLink toUploadDocuments(DocumentUpload documentUpload) {
        if (documentUpload == null) {
            return null;//NOSONAR
        }
        return DocumentLink.builder()
            .documentFilename(documentUpload.getFilename().trim())
            .documentUrl(documentUpload.getUrl().trim())
            .documentBinaryUrl(documentUpload.getUrl().trim() + "/" + BINARY_URL_SUFFIX)
            .build();
    }

    @FromDocumentLink
    public DocumentUpload fromDocumentLink(DocumentLink documentLink) {
        if (documentLink == null) {
            return null;//NOSONAR
        }
        return DocumentUpload.builder()
            .url(documentLink.getDocumentUrl())
            .filename(documentLink.getDocumentFilename())
            .build();
    }
}
