package uk.gov.hmcts.probate.core.service.mapper;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntestacyMapperTest {

    @Autowired
    private IntestacyMapper mapper;

    private IntestacyForm intestacyForm;
    private GrantOfRepresentationData grantOfRepresentation;

    @Before
    public void setUp() {
        intestacyForm = IntestacyTestDataCreator.createIntestacyForm();
        grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
    }

//    @Test
//    public void shouldMapIntestacyFormToGrantOfRepresentation() {
//        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(intestacyForm);
//        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(grantOfRepresentation);
//    }
//
//    @Test
//    public void shouldMapGrantOfRepresentationToGrantOfIntestacyForm() {
//        IntestacyForm actualIntestacyForm = mapper.fromCaseData(grantOfRepresentation);
//        actualIntestacyForm.setDeclaration(null);
//        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(intestacyForm);
//    }

    @Test
    public void shouldMapNullIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(null);
        Assert.assertThat(actualGrantOfRepresentation, is(nullValue()));
    }

    @Test
    public void shouldMapNullGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(null);
        Assert.assertThat(actualIntestacyForm, is(nullValue()));
    }

    @Test
    public void shouldMapEmptyIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentationData expectedGrantOfRepresentation = new GrantOfRepresentationData();
        expectedGrantOfRepresentation.setBoDocumentsUploaded(new ArrayList<>());
        expectedGrantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        expectedGrantOfRepresentation.setGrantType(GrantType.INTESTACY);
        expectedGrantOfRepresentation.setDeceasedMaritalStatus(MaritalStatus.MARRIED);
        expectedGrantOfRepresentation.setDeceasedSpouseNotApplyingReason(SpouseNotApplyingReason.RENUNCIATED);
        expectedGrantOfRepresentation.setStatementOfTruthDocument(DocumentLink.builder().documentFilename("filename").documentUrl("url").documentBinaryUrl("url/binary").build());
        IntestacyForm iform = IntestacyForm.builder().deceased(IntestacyDeceased.builder().maritalStatus("Married or in a civil partnership").build()).statementOfTruthDocument(DocumentUpload.builder().filename("filename").url("url").build()).applicant(IntestacyApplicant.builder().spouseNotApplyingReason("They don&rsquo;t want to apply and they give up the right to apply in the future (this is known as &lsquo;renunciation&rsquo;)").build()).build();
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(iform);
        Assert.assertThat(actualGrantOfRepresentation, equalTo(expectedGrantOfRepresentation));
        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(expectedGrantOfRepresentation);

    }

    @Test
    public void shouldMapEmptyGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm expectedIntestacyForm = new IntestacyForm();
        expectedIntestacyForm.setType(ProbateType.INTESTACY);
        expectedIntestacyForm.setCaseType(GrantType.INTESTACY.getName());
        expectedIntestacyForm.setCopies(new Copies());
        expectedIntestacyForm.setIht(new InheritanceTax());
        expectedIntestacyForm.setRegistry(new Registry());
        expectedIntestacyForm.setApplicant(new IntestacyApplicant());
        expectedIntestacyForm.setDeceased(new IntestacyDeceased());
        expectedIntestacyForm.setDeclaration(new Declaration());
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(new GrantOfRepresentationData());
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(expectedIntestacyForm);
    }

    @Test
    public void shouldMarital() {

        assertThat(MaritalStatus.WIDOWED).isEqualTo(MaritalStatus.fromString("widowed"));
        assertThat(SpouseNotApplyingReason.RENUNCIATED).isEqualTo(SpouseNotApplyingReason.fromString(SpouseNotApplyingReason.RENUNCIATED.getDescription()));
    }
}
