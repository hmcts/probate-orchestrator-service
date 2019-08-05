package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromUploadDocs;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToUploadDocs;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.cases.DocumentType;
import uk.gov.hmcts.reform.probate.model.cases.UploadDocument;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;
import uk.gov.hmcts.reform.probate.model.forms.Documents;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentsMapper {

    private static final String BINARY_URL_SUFFIX = "binary";

    @ToUploadDocs
    public List<CollectionMember<UploadDocument>> toUploadDocuments(Documents documents) {
        if (documents == null || CollectionUtils.isEmpty(documents.getUploads())) {
            return Lists.newArrayList();//NOSONAR
        }
        return documents.getUploads()
            .stream()
            .map(this::mapUploadDocument)
            .map(uploadDocument -> CollectionMember.<UploadDocument>builder()
                .value(uploadDocument)
                .build())
            .collect(Collectors.toList());
    }

    @FromUploadDocs
    public Documents fromUploadDocs(List<CollectionMember<UploadDocument>> collectionMembers) {
        if (CollectionUtils.isEmpty(collectionMembers)) {
            return Documents.builder().uploads(Lists.newArrayList()).build();//NOSONAR
        }
        List<DocumentUpload> documentUploads = collectionMembers
            .stream()
            .map(CollectionMember::getValue)
            .map(this::mapDocumentUpload)
            .collect(Collectors.toList());

        return Documents.builder().uploads(documentUploads).build();
    }

    private UploadDocument mapUploadDocument(DocumentUpload documentUpload) {
        return UploadDocument.builder()
            .documentType(DocumentType.DEATH_CERT)
            .documentLink(DocumentLink.builder()
                .documentUrl(documentUpload.getUrl().trim())
                .documentBinaryUrl(documentUpload.getUrl().trim() + "/" + BINARY_URL_SUFFIX)
                .documentFilename(documentUpload.getFilename().trim())
                .build())
            .comment(documentUpload.getFilename().trim())
            .build();
    }


    private DocumentUpload mapDocumentUpload(UploadDocument uploadDocument) {
        return DocumentUpload.builder()
            .filename(uploadDocument.getDocumentLink().getDocumentFilename())
            .url(uploadDocument.getDocumentLink().getDocumentUrl())
            .build();
    }
}
