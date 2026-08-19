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

    private final ExecutorApplyingMapperImpl executorApplyingMapper = new ExecutorApplyingMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executorApplyingMapper, "addressMapper", new AddressMapper());
    }

    @Test
    void shouldMapAllDedicatedParentFieldsDirectlyAndRoundTrip() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        source.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptedIn(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptedOut(Boolean.FALSE);
        source.setHalfNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        source.setHalfNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setHalfNieceOrNephewParentAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        assertNotNull(mapped.getValue().getApplicantFamilyDetails());

        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE,
            mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedOut());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getHalfNieceOrNephewParentAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedOut());
        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedOut());
    }

    @Test
    void shouldKeepSiblingParentAndNieceNephewOwnAdoptionFieldsSeparate() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        source.setWholeNieceOrNephewParentDieBeforeDeceased(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedIn(Boolean.TRUE);
        source.setWholeNieceOrNephewParentAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setWholeNieceOrNephewParentAdoptedOut(Boolean.TRUE);

        source.setWholeBloodSiblingDiedBeforeDeceased(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedIn(Boolean.FALSE);
        source.setWholeBloodSiblingAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedOut(Boolean.FALSE);

        source.setWholeBloodNieceOrNephewAdoptedIn(Boolean.TRUE);
        source.setWholeBloodNieceOrNephewAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setWholeBloodNieceOrNephewAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);

        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeNieceOrNephewParentAdoptedOut());

        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE,
            mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodSiblingAdoptedOut());

        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedIn());
        assertEquals(Boolean.FALSE,
            mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedOut());

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentAdoptedOut());

        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodSiblingDiedBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTripped.getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodSiblingAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTripped.getWholeBloodSiblingAdoptedOut());

        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodNieceOrNephewAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodNieceOrNephewAdoptedOut());
    }

    @Test
    void shouldUseLegacyWholeSiblingValuesWhenDedicatedWholeParentValuesAreMissing() {
        Executor source = buildCoApplicant(WHOLE_BLOOD_NIECE_NEPHEW);
        source.setWholeBloodSiblingDiedBeforeDeceased(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedIn(Boolean.FALSE);
        source.setWholeBloodSiblingAdoptionInEnglandOrWales(Boolean.TRUE);
        source.setWholeBloodSiblingAdoptedOut(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        mapped.getValue().getApplicantFamilyDetails().setWholeNieceOrNephewParentDieBeforeDeceased(null);
        mapped.getValue().getApplicantFamilyDetails().setWholeNieceOrNephewParentAdoptedIn(null);
        mapped.getValue().getApplicantFamilyDetails().setWholeNieceOrNephewParentAdoptionInEnglandOrWales(null);
        mapped.getValue().getApplicantFamilyDetails().setWholeNieceOrNephewParentAdoptedOut(null);

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);

        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE, roundTripped.getWholeNieceOrNephewParentAdoptedOut());
    }

    @Test
    void shouldUseLegacyHalfSiblingValuesWhenDedicatedHalfParentValuesAreMissing() {
        Executor source = buildCoApplicant(HALF_BLOOD_NIECE_NEPHEW);
        source.setHalfBloodSiblingDiedBeforeDeceased(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedIn(Boolean.TRUE);
        source.setHalfBloodSiblingAdoptionInEnglandOrWales(Boolean.FALSE);
        source.setHalfBloodSiblingAdoptedOut(Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        mapped.getValue().getApplicantFamilyDetails().setHalfNieceOrNephewParentDieBeforeDeceased(null);
        mapped.getValue().getApplicantFamilyDetails().setHalfNieceOrNephewParentAdoptedIn(null);
        mapped.getValue().getApplicantFamilyDetails().setHalfNieceOrNephewParentAdoptionInEnglandOrWales(null);
        mapped.getValue().getApplicantFamilyDetails().setHalfNieceOrNephewParentAdoptedOut(null);

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);

        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentDieBeforeDeceased());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getHalfNieceOrNephewParentAdoptionInEnglandOrWales());
        assertEquals(Boolean.TRUE, roundTripped.getHalfNieceOrNephewParentAdoptedOut());
    }

    @Test
    void shouldNotUseLegacySiblingFallbackWhenRelationshipIsNotNieceOrNephew() {
        Executor source = buildCoApplicant(null);
        source.setWholeBloodSiblingAdoptedIn(Boolean.TRUE);
        source.setHalfBloodSiblingAdoptedIn(Boolean.FALSE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
        mapped.getValue().getApplicantFamilyDetails().setWholeNieceOrNephewParentAdoptedIn(null);
        mapped.getValue().getApplicantFamilyDetails().setHalfNieceOrNephewParentAdoptedIn(null);

        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);

        assertNull(roundTripped.getWholeNieceOrNephewParentAdoptedIn());
        assertNull(roundTripped.getHalfNieceOrNephewParentAdoptedIn());
        assertEquals(Boolean.TRUE, roundTripped.getWholeBloodSiblingAdoptedIn());
        assertEquals(Boolean.FALSE, roundTripped.getHalfBloodSiblingAdoptedIn());
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
