package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExecutorApplyingMapperTest {

    private static final String WHOLE_BLOOD_NIECE_NEPHEW = "optionWholeBloodNieceOrNephew";
    private static final String HALF_BLOOD_NIECE_NEPHEW = "optionHalfBloodNieceOrNephew";
    private static final String WHOLE_BLOOD_SIBLING = "optionWholeBloodSibling";
    private static final String HALF_BLOOD_SIBLING = "optionHalfBloodSibling";

    private final ExecutorApplyingMapperImpl executorApplyingMapper = new ExecutorApplyingMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executorApplyingMapper, "addressMapper", new AddressMapper());
    }

    @Test
    void shouldMapWholeNieceNephewParentFieldsToWholeSiblingCaseFieldsAndBackToParentFields() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        source.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptedIn(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptedOut(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        assertNotNull(mapped.getValue().getApplicantFamilyDetails());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE,
            mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedOut());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentDieBeforeDeceased());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedOut());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedOut());
        assertNull(roundTripped.getWholeBloodSiblingDiedBeforeDeceased());
        assertNull(roundTripped.getWholeBloodSiblingAdoptedIn());
        assertNull(roundTripped.getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertNull(roundTripped.getWholeBloodSiblingAdoptedOut());
    }

    @Test
    void shouldMapHalfNieceNephewParentFieldsToHalfSiblingCaseFieldsAndBackToParentFields() {
        Executor source = buildCoApplicant(HALF_BLOOD_NIECE_NEPHEW);
        source.setHalfNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        assertNotNull(mapped.getValue().getApplicantFamilyDetails());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedOut());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentDieBeforeDeceased());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedOut());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfBloodNieceOrNephewAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfBloodNieceOrNephewAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedOut());
        assertNull(roundTripped.getHalfBloodSiblingDiedBeforeDeceased());
        assertNull(roundTripped.getHalfBloodSiblingAdoptedIn());
        assertNull(roundTripped.getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertNull(roundTripped.getHalfBloodSiblingAdoptedOut());
    }

    @Test
    void shouldPreferWholeParentFieldsForWholeNieceNephewWhenBothFieldFamiliesArePresent() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        source.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedOut(Boolean.TRUE);
        source.setWholeBloodSiblingDiedBeforeDeceased(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedIn(Boolean.FALSE);
        source.setWholeBloodSiblingAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedOut(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);

        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedOut());
    }

    @Test
    void shouldPreferHalfParentFieldsForHalfNieceNephewWhenBothFieldFamiliesArePresent() {
        Executor source = buildCoApplicant(HALF_BLOOD_NIECE_NEPHEW);
        source.setHalfNieceOrNephewParentDieBeforeDeceased(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptedIn(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptedOut(Boolean.FALSE);
        source.setHalfBloodSiblingDiedBeforeDeceased(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedIn(Boolean.TRUE);
        source.setHalfBloodSiblingAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);

        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE,
            mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedOut());
    }

    @Test
    void shouldKeepWholeSiblingMappingsForGenuineWholeSiblingRelationship() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_SIBLING);
        source.setWholeBloodSiblingDiedBeforeDeceased(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedIn(Boolean.FALSE);
        source.setWholeBloodSiblingAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedOut(Boolean.FALSE);
        source.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE,
            mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTripped.getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTripped.getWholeBloodSiblingAdoptedOut());
        assertNull(roundTripped.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertNull(roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertNull(roundTripped.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(roundTripped.getWholeNieceOrNephewParentAdoptedOut());
    }

    @Test
    void shouldKeepHalfSiblingMappingsForGenuineHalfSiblingRelationship() {
        Executor source = buildCoApplicant(HALF_BLOOD_SIBLING);
        source.setHalfBloodSiblingDiedBeforeDeceased(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedIn(Boolean.TRUE);
        source.setHalfBloodSiblingAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedOut(Boolean.TRUE);
        source.setHalfNieceOrNephewParentDieBeforeDeceased(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptedIn(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptedOut(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.FALSE, roundTripped.getHalfBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTripped.getHalfBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getHalfBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getHalfBloodSiblingAdoptedOut());
        assertNull(roundTripped.getHalfNieceOrNephewParentDieBeforeDeceased());
        assertNull(roundTripped.getHalfNieceOrNephewParentAdoptedIn());
        assertNull(roundTripped.getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertNull(roundTripped.getHalfNieceOrNephewParentAdoptedOut());
    }

    @Test
    void shouldMapExecutorWithoutRelationshipWithoutUsingParentAliasFields() {
        Executor source = buildCoApplicant(null);
        source.setWholeBloodSiblingDiedBeforeDeceased(Boolean.TRUE);
        source.setHalfBloodSiblingAdoptedIn(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);

        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfBloodSiblingAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentDieBeforeDeceased());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedIn());
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
