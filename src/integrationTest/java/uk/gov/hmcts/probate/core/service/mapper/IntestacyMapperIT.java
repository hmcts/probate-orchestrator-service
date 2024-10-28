package uk.gov.hmcts.probate.core.service.mapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import uk.gov.hmcts.reform.probate.model.forms.Equality;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.ProvideInformation;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.ReviewResponse;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntestacyMapperIT {

    @Autowired
    private IntestacyMapper mapper;

    private IntestacyForm intestacyForm;
    private GrantOfRepresentationData grantOfRepresentation;

    @BeforeEach
    public void setUp() {
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
        assertNull(actualGrantOfRepresentation);
    }

    @Test
    public void shouldMapNullGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(null);
        assertNull(actualIntestacyForm);
    }

    @Test
    public void shouldMapEmptyIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentationData expectedGrantOfRepresentation = new GrantOfRepresentationData();
        expectedGrantOfRepresentation.setCitizenDocumentsUploaded(new ArrayList<>());
        expectedGrantOfRepresentation.setApplicationSubmittedDate(LocalDate.now());
        expectedGrantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        expectedGrantOfRepresentation.setGrantType(GrantType.INTESTACY);
        expectedGrantOfRepresentation.setDeceasedMaritalStatus(MaritalStatus.MARRIED);
        expectedGrantOfRepresentation.setDeceasedSpouseNotApplyingReason(SpouseNotApplyingReason.RENUNCIATED);
        expectedGrantOfRepresentation.setStatementOfTruthDocument(
            DocumentLink.builder().documentFilename("filename").documentUrl("url").documentBinaryUrl("url/binary")
                .build());
        IntestacyForm iform =
            IntestacyForm.builder().deceased(IntestacyDeceased.builder().maritalStatus("optionMarried").build())
                .statementOfTruthDocument(DocumentUpload.builder().filename("filename").url("url").build())
                .applicant(IntestacyApplicant.builder().spouseNotApplyingReason("optionRenouncing").build()).build();
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(iform);
        assertEquals(expectedGrantOfRepresentation, actualGrantOfRepresentation);
        assertThat(actualGrantOfRepresentation)
            .isEqualToComparingFieldByFieldRecursively(expectedGrantOfRepresentation);

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
        expectedIntestacyForm.setLanguage(new Language());
        expectedIntestacyForm.setEquality(new Equality());
        expectedIntestacyForm.setProvideinformation(new ProvideInformation());
        expectedIntestacyForm.setReviewresponse(new ReviewResponse());
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(new GrantOfRepresentationData());
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(expectedIntestacyForm);
    }

    @Test
    public void shouldMarital() {

        assertThat(MaritalStatus.WIDOWED).isEqualTo(MaritalStatus.fromString("optionWidowed"));
        assertThat(SpouseNotApplyingReason.RENUNCIATED)
            .isEqualTo(SpouseNotApplyingReason.fromString(SpouseNotApplyingReason.RENUNCIATED.getDescription()));
    }
}
