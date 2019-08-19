package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;

public class StatementOfTruthMapperTest {

    public static final String FILENAME = "filename";
    public static final String URL = "url";
    StatementOfTruthMapper statementOfTruthMapper = new StatementOfTruthMapper();

    @Test
    void shouldMapToUploadDocuments() {

        DocumentLink documentLink = statementOfTruthMapper.toUploadDocuments(null);
        Assert.assertThat(documentLink, CoreMatchers.nullValue());

        DocumentLink documentLink1 = statementOfTruthMapper.toUploadDocuments(
                DocumentUpload.builder().filename(FILENAME).url(URL).build());
        Assert.assertThat(documentLink1.getDocumentFilename(), CoreMatchers.equalTo(FILENAME));
        Assert.assertThat(documentLink1.getDocumentUrl(), CoreMatchers.equalTo(URL));
        Assert.assertThat(documentLink1.getDocumentBinaryUrl(), CoreMatchers.equalTo("url/binary"));
    }

    @Test
    void shouldMapFromDocumentLink() {

        DocumentUpload documentUpload = statementOfTruthMapper.fromDocumentLink(null);
        Assert.assertThat(documentUpload, CoreMatchers.nullValue());

        DocumentUpload documentUpload1 = statementOfTruthMapper.fromDocumentLink(DocumentLink.builder()
                .documentFilename(FILENAME)
              .documentUrl(URL).build());
        Assert.assertThat(documentUpload1.getFilename(), CoreMatchers.equalTo(FILENAME));
           Assert.assertThat(documentUpload1.getUrl(), CoreMatchers.equalTo(URL));
    }
}