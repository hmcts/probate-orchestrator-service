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
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;
import uk.gov.hmcts.reform.probate.model.forms.Equality;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.ProvideInformation;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.ReviewResponse;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.CoApplicants;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;


import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntestacyMapperIT {

    private static final String WHOLE_BLOOD_NIECE_NEPHEW = "optionWholeBloodNieceOrNephew";
    private static final String HALF_BLOOD_NIECE_NEPHEW = "optionHalfBloodNieceOrNephew";

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
        Declaration declaration = new Declaration();
        declaration.setDeclaration(DeclarationDeclaration.builder().build());
        declaration.setLegalStatement(LegalStatement.builder().build());
        expectedIntestacyForm.setDeclaration(declaration);
        expectedIntestacyForm.setLanguage(new Language());
        expectedIntestacyForm.setEquality(new Equality());
        expectedIntestacyForm.setProvideinformation(new ProvideInformation());
        expectedIntestacyForm.setReviewresponse(new ReviewResponse());
        expectedIntestacyForm.setAssets(new PaAssets());
        expectedIntestacyForm.setExecutors(new CoApplicants());
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(new GrantOfRepresentationData());
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(expectedIntestacyForm);
    }

    @Test
    public void shouldMarital() {

        assertThat(MaritalStatus.WIDOWED).isEqualTo(MaritalStatus.fromString("optionWidowed"));
        assertThat(SpouseNotApplyingReason.RENUNCIATED)
            .isEqualTo(SpouseNotApplyingReason.fromString(SpouseNotApplyingReason.RENUNCIATED.getDescription()));
    }

    @Test
    public void shouldRoundTripWholeNieceNephewParentFieldsThroughSiblingCaseFields() {
        Executor coApplicant = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        coApplicant.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.TRUE);
        coApplicant.setWholeNieceOrNephewParentAdoptedIn(Boolean.FALSE);
        coApplicant.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.TRUE);
        coApplicant.setWholeNieceOrNephewParentAdoptedOut(Boolean.FALSE);

        IntestacyForm sourceForm = buildFormWithCoApplicant(coApplicant);
        GrantOfRepresentationData caseData = mapper.toCaseData(sourceForm);
        ExecutorApplying mappedCoApplicant = findCoApplicantCaseData(caseData);

        assertNotNull(mappedCoApplicant.getApplicantFamilyDetails());
        assertEquals(Boolean.TRUE, mappedCoApplicant.getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mappedCoApplicant.getApplicantFamilyDetails().getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE,
            mappedCoApplicant.getApplicantFamilyDetails().getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mappedCoApplicant.getApplicantFamilyDetails().getWholeBloodSiblingAdoptedOut());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getWholeNieceOrNephewParentDieBeforeDeceased());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedIn());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedOut());

        IntestacyForm roundTrippedForm = mapper.fromCaseData(caseData);
        Executor roundTrippedCoApplicant = findCoApplicant(roundTrippedForm);

        assertEquals(Boolean.TRUE, roundTrippedCoApplicant.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTrippedCoApplicant.getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE, roundTrippedCoApplicant.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTrippedCoApplicant.getWholeNieceOrNephewParentAdoptedOut());
        assertNull(roundTrippedCoApplicant.getWholeBloodSiblingDiedBeforeDeceased());
        assertNull(roundTrippedCoApplicant.getWholeBloodSiblingAdoptedIn());
        assertNull(roundTrippedCoApplicant.getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertNull(roundTrippedCoApplicant.getWholeBloodSiblingAdoptedOut());
    }

    @Test
    public void shouldRoundTripHalfNieceNephewParentFieldsThroughSiblingCaseFields() {
        Executor coApplicant = buildCoApplicant(HALF_BLOOD_NIECE_NEPHEW);
        coApplicant.setHalfNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        coApplicant.setHalfNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        coApplicant.setHalfNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        coApplicant.setHalfNieceOrNephewParentAdoptedOut(Boolean.TRUE);

        IntestacyForm sourceForm = buildFormWithCoApplicant(coApplicant);
        GrantOfRepresentationData caseData = mapper.toCaseData(sourceForm);
        ExecutorApplying mappedCoApplicant = findCoApplicantCaseData(caseData);

        assertNotNull(mappedCoApplicant.getApplicantFamilyDetails());
        assertEquals(Boolean.FALSE, mappedCoApplicant.getApplicantFamilyDetails().getHalfBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.TRUE, mappedCoApplicant.getApplicantFamilyDetails().getHalfBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE,
            mappedCoApplicant.getApplicantFamilyDetails().getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mappedCoApplicant.getApplicantFamilyDetails().getHalfBloodSiblingAdoptedOut());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getHalfNieceOrNephewParentDieBeforeDeceased());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedIn());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(mappedCoApplicant.getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedOut());

        IntestacyForm roundTrippedForm = mapper.fromCaseData(caseData);
        Executor roundTrippedCoApplicant = findCoApplicant(roundTrippedForm);

        assertEquals(Boolean.FALSE, roundTrippedCoApplicant.getHalfNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTrippedCoApplicant.getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE, roundTrippedCoApplicant.getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTrippedCoApplicant.getHalfNieceOrNephewParentAdoptedOut());
        assertNull(roundTrippedCoApplicant.getHalfBloodSiblingDiedBeforeDeceased());
        assertNull(roundTrippedCoApplicant.getHalfBloodSiblingAdoptedIn());
        assertNull(roundTrippedCoApplicant.getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertNull(roundTrippedCoApplicant.getHalfBloodSiblingAdoptedOut());
    }

    private Executor findCoApplicant(IntestacyForm form) {
        assertThat(form.getExecutors()).isNotNull();
        assertThat(form.getExecutors().getList()).isNotNull();

        Optional<Executor> coApplicant = form.getExecutors().getList().stream()
            .filter(executor -> Boolean.FALSE.equals(executor.getIsApplicant()))
            .findFirst();

        assertThat(coApplicant.isPresent()).isTrue();
        return coApplicant.get();
    }

    private ExecutorApplying findCoApplicantCaseData(GrantOfRepresentationData caseData) {
        assertThat(caseData.getExecutorsApplying()).isNotNull();

        Optional<ExecutorApplying> coApplicant = caseData.getExecutorsApplying().stream()
            .map(uk.gov.hmcts.reform.probate.model.cases.CollectionMember::getValue)
            .filter(executorApplying -> Boolean.FALSE.equals(executorApplying.getApplyingExecutorApplicant()))
            .findFirst();

        assertThat(coApplicant.isPresent()).isTrue();
        return coApplicant.get();
    }

    private IntestacyForm buildFormWithCoApplicant(Executor coApplicant) {
        Executor primaryApplicant = Executor.builder()
            .firstName("Primary")
            .lastName("Applicant")
            .fullName("Primary Applicant")
            .isApplying(Boolean.TRUE)
            .isApplicant(Boolean.TRUE)
            .build();

        return IntestacyForm.builder()
            .applicant(IntestacyApplicant.builder().firstName("Primary").lastName("Applicant").build())
            .executors(CoApplicants.builder()
                .hasCoApplicant(Boolean.TRUE)
                .list(java.util.List.of(primaryApplicant, coApplicant))
                .build())
            .build();
    }

    private Executor buildCoApplicant(String relationship) {
        return Executor.builder()
            .firstName("Case")
            .lastName("Coapplicant")
            .fullName("Case Coapplicant")
            .isApplying(Boolean.TRUE)
            .isApplicant(Boolean.FALSE)
            .coApplicantRelationshipToDeceased(relationship)
            .build();
    }


}
