package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatementOfTruthMapperTest {

    public static final String FILENAME = "filename";
    public static final String URL = "url";
    StatementOfTruthMapper statementOfTruthMapper = new StatementOfTruthMapper();

    @Test
    void shouldMapToUploadDocuments() {

        DocumentLink documentLink = statementOfTruthMapper.toUploadDocuments(null);
        assertEquals(documentLink, CoreMatchers.nullValue());

        DocumentLink documentLink1 = statementOfTruthMapper.toUploadDocuments(
                DocumentUpload.builder().filename(FILENAME).url(URL).build());
        assertEquals(documentLink1.getDocumentFilename(), CoreMatchers.equalTo(FILENAME));
        assertEquals(documentLink1.getDocumentUrl(), CoreMatchers.equalTo(URL));
        assertEquals(documentLink1.getDocumentBinaryUrl(), CoreMatchers.equalTo("url/binary"));
    }

    @Test
    void shouldMapFromDocumentLink() {

        DocumentUpload documentUpload = statementOfTruthMapper.fromDocumentLink(null);
        assertEquals(documentUpload, CoreMatchers.nullValue());

        DocumentUpload documentUpload1 = statementOfTruthMapper.fromDocumentLink(DocumentLink.builder()
                .documentFilename(FILENAME)
              .documentUrl(URL).build());
        assertEquals(documentUpload1.getFilename(), CoreMatchers.equalTo(FILENAME));
        assertEquals(documentUpload1.getUrl(), CoreMatchers.equalTo(URL));
    }
}
