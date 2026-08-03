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
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldRoundTripParentDeathAndAdoptionFieldsThroughIntestacyMapperForWholeBloodCoApplicant() {
        verifyRoundTripForRelationship(WHOLE_BLOOD_NIECE_NEPHEW);
    }

    @Test
    public void shouldRoundTripParentDeathAndAdoptionFieldsThroughIntestacyMapperForHalfBloodCoApplicant() {
        verifyRoundTripForRelationship(HALF_BLOOD_NIECE_NEPHEW);
    }

    private void verifyRoundTripForRelationship(String relationship) {
        for (FieldMapping mapping : fieldMappings()) {
            for (Boolean value : Arrays.asList(Boolean.TRUE, Boolean.FALSE, null)) {
                Executor coApplicant = buildCoApplicant(relationship);
                mapping.setter.accept(coApplicant, value);

                IntestacyForm sourceForm = buildFormWithCoApplicant(coApplicant);
                GrantOfRepresentationData caseData = mapper.toCaseData(sourceForm);
                IntestacyForm roundTrippedForm = mapper.fromCaseData(caseData);

                Executor roundTrippedCoApplicant = findCoApplicant(roundTrippedForm);
                assertEquals(value, mapping.getter.apply(roundTrippedCoApplicant),
                    "Intestacy mapper chain mismatch for field: " + mapping.fieldName + " relationship: "
                        + relationship);
            }
        }
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

    private java.util.List<FieldMapping> fieldMappings() {
        return java.util.List.of(
            new FieldMapping("wholeBloodSiblingDiedBeforeDeceased",
                Executor::setWholeBloodSiblingDiedBeforeDeceased,
                Executor::getWholeBloodSiblingDiedBeforeDeceased),
            new FieldMapping("wholeBloodNieceOrNephewAdoptedIn",
                Executor::setWholeBloodNieceOrNephewAdoptedIn,
                Executor::getWholeBloodNieceOrNephewAdoptedIn),
            new FieldMapping("wholeBloodNieceOrNephewAdoptionInEnglandOrWales",
                Executor::setWholeBloodNieceOrNephewAdoptionInEnglandOrWales,
                Executor::getWholeBloodNieceOrNephewAdoptionInEnglandOrWales),
            new FieldMapping("wholeBloodNieceOrNephewAdoptedOut",
                Executor::setWholeBloodNieceOrNephewAdoptedOut,
                Executor::getWholeBloodNieceOrNephewAdoptedOut),
            new FieldMapping("halfBloodSiblingDiedBeforeDeceased",
                Executor::setHalfBloodSiblingDiedBeforeDeceased,
                Executor::getHalfBloodSiblingDiedBeforeDeceased),
            new FieldMapping("halfBloodNieceOrNephewAdoptedIn",
                Executor::setHalfBloodNieceOrNephewAdoptedIn,
                Executor::getHalfBloodNieceOrNephewAdoptedIn),
            new FieldMapping("halfBloodNieceOrNephewAdoptionInEnglandOrWales",
                Executor::setHalfBloodNieceOrNephewAdoptionInEnglandOrWales,
                Executor::getHalfBloodNieceOrNephewAdoptionInEnglandOrWales),
            new FieldMapping("halfBloodNieceOrNephewAdoptedOut",
                Executor::setHalfBloodNieceOrNephewAdoptedOut,
                Executor::getHalfBloodNieceOrNephewAdoptedOut)
        );
    }

    private static final class FieldMapping {
        private final String fieldName;
        private final BiConsumer<Executor, Boolean> setter;
        private final Function<Executor, Boolean> getter;

        private FieldMapping(String fieldName, BiConsumer<Executor, Boolean> setter, Function<Executor, Boolean> getter) {
            this.fieldName = fieldName;
            this.setter = setter;
            this.getter = getter;
        }
    }

}
