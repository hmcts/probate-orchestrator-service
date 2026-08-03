package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExecutorApplyingMapperTest {

    private static final String WHOLE_BLOOD_NIECE_NEPHEW = "optionWholeBloodNieceOrNephew";
    private static final String HALF_BLOOD_NIECE_NEPHEW = "optionHalfBloodNieceOrNephew";

    private final ExecutorApplyingMapperImpl executorApplyingMapper = new ExecutorApplyingMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executorApplyingMapper, "addressMapper", new AddressMapper());
    }

    @Test
    void shouldRoundTripParentDeathAndAdoptionFieldsForWholeBloodCoApplicant() {
        verifyRoundTripForRelationship(WHOLE_BLOOD_NIECE_NEPHEW);
    }

    @Test
    void shouldRoundTripParentDeathAndAdoptionFieldsForHalfBloodCoApplicant() {
        verifyRoundTripForRelationship(HALF_BLOOD_NIECE_NEPHEW);
    }

    private void verifyRoundTripForRelationship(String relationship) {
        List<Boolean> values = Arrays.asList(Boolean.TRUE, Boolean.FALSE, null);

        for (FieldMapping mapping : fieldMappings()) {
            for (Boolean value : values) {
                Executor source = buildCoApplicant(relationship);
                mapping.setter.accept(source, value);

                CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(source);
                assertNotNull(mapped.getValue().getApplicantFamilyDetails(),
                    "Expected applicantFamilyDetails for field: " + mapping.fieldName);
                assertEquals(value, mapping.caseGetter.apply(mapped.getValue()),
                    "Case mapping mismatch for field: " + mapping.fieldName + " relationship: " + relationship);

                Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);
                assertEquals(value, mapping.executorGetter.apply(roundTripped),
                    "Round-trip mismatch for field: " + mapping.fieldName + " relationship: " + relationship);
            }
        }
    }

    private List<FieldMapping> fieldMappings() {
        return List.of(
            new FieldMapping(
                "wholeBloodSiblingDiedBeforeDeceased",
                Executor::setWholeBloodSiblingDiedBeforeDeceased,
                Executor::getWholeBloodSiblingDiedBeforeDeceased,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getWholeBloodSiblingDiedBeforeDeceased()
            ),
            new FieldMapping(
                "wholeBloodNieceOrNephewAdoptedIn",
                Executor::setWholeBloodNieceOrNephewAdoptedIn,
                Executor::getWholeBloodNieceOrNephewAdoptedIn,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedIn()
            ),
            new FieldMapping(
                "wholeBloodNieceOrNephewAdoptionInEnglandOrWales",
                Executor::setWholeBloodNieceOrNephewAdoptionInEnglandOrWales,
                Executor::getWholeBloodNieceOrNephewAdoptionInEnglandOrWales,
                executorApplying -> executorApplying.getApplicantFamilyDetails()
                    .getWholeBloodNieceOrNephewAdoptionInEnglandOrWales()
            ),
            new FieldMapping(
                "wholeBloodNieceOrNephewAdoptedOut",
                Executor::setWholeBloodNieceOrNephewAdoptedOut,
                Executor::getWholeBloodNieceOrNephewAdoptedOut,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedOut()
            ),
            new FieldMapping(
                "halfBloodSiblingDiedBeforeDeceased",
                Executor::setHalfBloodSiblingDiedBeforeDeceased,
                Executor::getHalfBloodSiblingDiedBeforeDeceased,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getHalfBloodSiblingDiedBeforeDeceased()
            ),
            new FieldMapping(
                "halfBloodNieceOrNephewAdoptedIn",
                Executor::setHalfBloodNieceOrNephewAdoptedIn,
                Executor::getHalfBloodNieceOrNephewAdoptedIn,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getHalfBloodNieceOrNephewAdoptedIn()
            ),
            new FieldMapping(
                "halfBloodNieceOrNephewAdoptionInEnglandOrWales",
                Executor::setHalfBloodNieceOrNephewAdoptionInEnglandOrWales,
                Executor::getHalfBloodNieceOrNephewAdoptionInEnglandOrWales,
                executorApplying -> executorApplying.getApplicantFamilyDetails()
                    .getHalfBloodNieceOrNephewAdoptionInEnglandOrWales()
            ),
            new FieldMapping(
                "halfBloodNieceOrNephewAdoptedOut",
                Executor::setHalfBloodNieceOrNephewAdoptedOut,
                Executor::getHalfBloodNieceOrNephewAdoptedOut,
                executorApplying -> executorApplying.getApplicantFamilyDetails().getHalfBloodNieceOrNephewAdoptedOut()
            )
        );
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

    private static final class FieldMapping {
        private final String fieldName;
        private final BiConsumer<Executor, Boolean> setter;
        private final Function<Executor, Boolean> executorGetter;
        private final Function<ExecutorApplying, Boolean> caseGetter;

        private FieldMapping(
            String fieldName,
            BiConsumer<Executor, Boolean> setter,
            Function<Executor, Boolean> executorGetter,
            Function<ExecutorApplying, Boolean> caseGetter
        ) {
            this.fieldName = fieldName;
            this.setter = setter;
            this.executorGetter = executorGetter;
            this.caseGetter = caseGetter;
        }
    }
}
