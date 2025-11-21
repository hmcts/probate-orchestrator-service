package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.ApplicantFamilyDetails;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.FamilyDetails;

class ApplicantFamilyDetailsMapperTest {
    @Mock
    private ApplicantFamilyDetailsMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ApplicantFamilyDetailsMapper();
    }

    @Test
    void toApplicantDetailsReturnsNullWhenInputIsNull() {
        ApplicantFamilyDetails result = mapper.toApplicantDetails(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    void fromApplicantDetailsReturnsNullWhenInputIsNull() {
        FamilyDetails result = mapper.fromApplicantDetails(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    void toApplicantDetailsMapsAllFieldsCorrectly() {
        FamilyDetails details = FamilyDetails.builder()
                .relationshipToDeceased("optionSpousePartner")
                .childAdoptedIn(true)
                .childAdoptionInEnglandOrWales(false)
                .childAdoptedOut(true)
                .grandchildAdoptedIn(false)
                .grandchildAdoptionInEnglandOrWales(true)
                .grandchildAdoptedOut(false)
                .build();

        ApplicantFamilyDetails result = mapper.toApplicantDetails(details);

        Assertions.assertThat(result.getRelationshipToDeceased()).isEqualTo(Relationship.PARTNER);
        Assertions.assertThat(result.getChildAdoptedIn()).isTrue();
        Assertions.assertThat(result.getChildAdoptionInEnglandOrWales()).isFalse();
        Assertions.assertThat(result.getChildAdoptedOut()).isTrue();
        Assertions.assertThat(result.getGrandchildAdoptedIn()).isFalse();
        Assertions.assertThat(result.getGrandchildAdoptionInEnglandOrWales()).isTrue();
        Assertions.assertThat(result.getGrandchildAdoptedOut()).isFalse();
    }

    @Test
    void fromApplicantDetailsMapsAllFieldsCorrectly() {
        ApplicantFamilyDetails details = ApplicantFamilyDetails.builder()
                .relationshipToDeceased(Relationship.PARTNER)
                .childAdoptedIn(true)
                .childAdoptionInEnglandOrWales(false)
                .childAdoptedOut(true)
                .grandchildAdoptedIn(false)
                .grandchildAdoptionInEnglandOrWales(true)
                .grandchildAdoptedOut(false)
                .build();

        FamilyDetails result = mapper.fromApplicantDetails(details);

        Assertions.assertThat(result.getRelationshipToDeceased()).isEqualTo("optionSpousePartner");
        Assertions.assertThat(result.getChildAdoptedIn()).isTrue();
        Assertions.assertThat(result.getChildAdoptionInEnglandOrWales()).isFalse();
        Assertions.assertThat(result.getChildAdoptedOut()).isTrue();
        Assertions.assertThat(result.getGrandchildAdoptedIn()).isFalse();
        Assertions.assertThat(result.getGrandchildAdoptionInEnglandOrWales()).isTrue();
        Assertions.assertThat(result.getGrandchildAdoptedOut()).isFalse();
    }

    @Test
    void fromApplicantDetailsHandlesOptionGrandchildRelationshipCorrectly() {
        ApplicantFamilyDetails details = ApplicantFamilyDetails.builder()
                .relationshipToDeceased(Relationship.GRANDCHILD)
                .grandchildParentAdoptedIn(true)
                .grandchildParentAdoptedOut(false)
                .grandchildParentAdoptionInEnglandOrWales(true)
                .build();

        FamilyDetails result = mapper.fromApplicantDetails(details);

        Assertions.assertThat(result.getAdoptedIn()).isTrue();
        Assertions.assertThat(result.getAdoptedOut()).isFalse();
        Assertions.assertThat(result.getAdoptionPlace()).isTrue();
    }

    @Test
    void fromApplicantDetailsHandlesOptionChildRelationshipCorrectly() {
        ApplicantFamilyDetails details = ApplicantFamilyDetails.builder()
                .relationshipToDeceased(Relationship.CHILD)
                .childAdoptedIn(true)
                .childAdoptedOut(false)
                .childAdoptionInEnglandOrWales(true)
                .build();

        FamilyDetails result = mapper.fromApplicantDetails(details);

        Assertions.assertThat(result.getAdoptedIn()).isTrue();
        Assertions.assertThat(result.getAdoptedOut()).isFalse();
        Assertions.assertThat(result.getAdoptionPlace()).isTrue();
    }

    @Test
    void fromApplicantDetailsHandlesOptionParentRelationshipCorrectly() {
        ApplicantFamilyDetails details = ApplicantFamilyDetails.builder()
                .relationshipToDeceased(Relationship.PARENT)
                .deceasedAdoptedIn(true)
                .deceasedAdoptedOut(false)
                .deceasedAdoptionInEnglandOrWales(true)
                .build();

        FamilyDetails result = mapper.fromApplicantDetails(details);

        Assertions.assertThat(result.getAdoptedIn()).isTrue();
        Assertions.assertThat(result.getAdoptedOut()).isFalse();
        Assertions.assertThat(result.getAdoptionPlace()).isTrue();
    }

    @Test
    void fromApplicantDetailsHandlesNullRelationshipGracefully() {
        ApplicantFamilyDetails details = ApplicantFamilyDetails.builder()
                .relationshipToDeceased(null)
                .build();

        FamilyDetails result = mapper.fromApplicantDetails(details);

        Assertions.assertThat(result.getAdoptedIn()).isNull();
        Assertions.assertThat(result.getAdoptedOut()).isNull();
        Assertions.assertThat(result.getAdoptionPlace()).isNull();
    }

}