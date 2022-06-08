package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatementOfTruthMapperTest {

    public static final String FILENAME = "filename";
    public static final String URL = "url";
    StatementOfTruthMapper statementOfTruthMapper = new StatementOfTruthMapper();

    @Test
    void shouldMapToUploadDocuments() {

        DocumentLink documentLink = statementOfTruthMapper.toUploadDocuments(null);
        assertNull(documentLink);

        DocumentLink documentLink1 = statementOfTruthMapper.toUploadDocuments(
                DocumentUpload.builder().filename(FILENAME).url(URL).build());
        assertEquals(FILENAME, documentLink1.getDocumentFilename());
        assertEquals(URL, documentLink1.getDocumentUrl());
        assertEquals("url/binary", documentLink1.getDocumentBinaryUrl());
    }

    @Test
    void shouldMapFromDocumentLink() {

        DocumentUpload documentUpload = statementOfTruthMapper.fromDocumentLink(null);
        assertNull(documentUpload);

        DocumentUpload documentUpload1 = statementOfTruthMapper.fromDocumentLink(DocumentLink.builder()
                .documentFilename(FILENAME)
              .documentUrl(URL).build());
        assertEquals(FILENAME, documentUpload1.getFilename());
        assertEquals(URL, documentUpload1.getUrl());
    }
}
