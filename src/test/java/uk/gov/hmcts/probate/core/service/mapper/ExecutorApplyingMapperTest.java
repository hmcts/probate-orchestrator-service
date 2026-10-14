package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExecutorApplyingMapperTest {

    private final ExecutorApplyingMapperImpl executorApplyingMapper = new ExecutorApplyingMapperImpl();
    private final ExecutorNotApplyingMapperImpl executorNotApplyingMapper = new ExecutorNotApplyingMapperImpl();
    private ExecutorsMapper executorsMapper;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executorApplyingMapper, "addressMapper", new AddressMapper());
        executorsMapper = new ExecutorsMapper(executorApplyingMapper, executorNotApplyingMapper);
    }

    @Test
    void shouldMapWholeBloodNieceOrNephewAdoptionAnswersBothWays() {
        Executor coApplicant = buildCoApplicant(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(coApplicant);
        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);

        assertEquals(coApplicant, roundTripped);
    }

    @Test
    void shouldKeepWholeBloodNieceOrNephewAdoptionAnswersNullWhenUnset() {
        Executor coApplicant = buildCoApplicant(null, null, null);

        CollectionMember<ExecutorApplying> mapped = executorApplyingMapper.toExecutorApplying(coApplicant);
        Executor roundTripped = executorApplyingMapper.fromExecutorApplying(mapped);

        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedIn());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertNull(mapped.getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedOut());

        assertNull(roundTripped.getWholeBloodNieceOrNephewAdoptedIn());
        assertNull(roundTripped.getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertNull(roundTripped.getWholeBloodNieceOrNephewAdoptedOut());
    }

    @Test
    void shouldMapWholeBloodNieceOrNephewAdoptionAnswersForCoApplicantOnlyInMixedCollection() {
        Executor primaryApplicant = Executor.builder()
            .firstName("Primary")
            .lastName("Applicant")
            .fullName("Primary Applicant")
            .isApplying(Boolean.TRUE)
            .isApplicant(Boolean.TRUE)
            .wholeBloodNieceOrNephewAdoptedIn(Boolean.TRUE)
            .wholeBloodNieceOrNephewAdoptionInEnglandOrWales(Boolean.TRUE)
            .wholeBloodNieceOrNephewAdoptedOut(Boolean.TRUE)
            .build();

        Executor coApplicant = buildCoApplicant(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);

        List<CollectionMember<ExecutorApplying>> mappedApplyingExecutors =
            executorsMapper.toExecutorApplyingCollectionMember(List.of(primaryApplicant, coApplicant));

        assertEquals(1, mappedApplyingExecutors.size());
        assertEquals(Boolean.FALSE,
            mappedApplyingExecutors.get(0).getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedIn());
        assertEquals(Boolean.TRUE,
            mappedApplyingExecutors.get(0).getValue().getApplicantFamilyDetails()
                .getWholeBloodNieceOrNephewAdoptionInEnglandOrWales());
        assertEquals(Boolean.FALSE,
            mappedApplyingExecutors.get(0).getValue().getApplicantFamilyDetails().getWholeBloodNieceOrNephewAdoptedOut());
    }

    private Executor buildCoApplicant(Boolean adoptedIn, Boolean adoptionInEnglandOrWales, Boolean adoptedOut) {
        return Executor.builder()
            .firstName("Case")
            .lastName("Coapplicant")
            .fullName("Case Coapplicant")
            .isApplying(Boolean.TRUE)
            .isApplicant(Boolean.FALSE)
            .coApplicantRelationshipToDeceased("optionWholeBloodNieceOrNephew")
            .wholeBloodNieceOrNephewAdoptedIn(adoptedIn)
            .wholeBloodNieceOrNephewAdoptionInEnglandOrWales(adoptionInEnglandOrWales)
            .wholeBloodNieceOrNephewAdoptedOut(adoptedOut)
            .build();
    }
}

