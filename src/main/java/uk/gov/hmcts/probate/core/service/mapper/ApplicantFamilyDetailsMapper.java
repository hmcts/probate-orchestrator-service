package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromApplicantFamilyDetails;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToApplicantFamilyDetails;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.ApplicantFamilyDetails;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.FamilyDetails;

@Component
public class ApplicantFamilyDetailsMapper {
    @ToApplicantFamilyDetails
    public ApplicantFamilyDetails toApplicantDetails(FamilyDetails details) {
        if (details == null) {
            return null;
        }
        return ApplicantFamilyDetails.builder()
                .relationshipToDeceased(Relationship.fromString(details.getRelationshipToDeceased()))
                .childAdoptedIn(details.getChildAdoptedIn())
                .childAdoptionInEnglandOrWales(details.getChildAdoptionInEnglandOrWales())
                .childAdoptedOut(details.getChildAdoptedOut())
                .grandchildAdoptedIn(details.getGrandchildAdoptedIn())
                .grandchildAdoptionInEnglandOrWales(details.getGrandchildAdoptionInEnglandOrWales())
                .grandchildAdoptedOut(details.getGrandchildAdoptedOut())
                .grandchildParentAdoptedIn(details.getGrandchildParentAdoptedIn())
                .grandchildParentAdoptionInEnglandOrWales(details.getGrandchildParentAdoptionInEnglandOrWales())
                .grandchildParentAdoptedOut(details.getGrandchildParentAdoptedOut())
                .deceasedAdoptedIn(details.getDeceasedAdoptedIn())
                .deceasedAdoptionInEnglandOrWales(details.getDeceasedAdoptionInEnglandOrWales())
                .deceasedAdoptedOut(details.getDeceasedAdoptedOut())
                .build();
    }

    @FromApplicantFamilyDetails
    public FamilyDetails fromApplicantDetails(ApplicantFamilyDetails details) {
        if (details == null) {
            return null;
        }
        String relationship = details.getRelationshipToDeceased() != null
                ? details.getRelationshipToDeceased().getDescription() : null;

        FamilyDetails.FamilyDetailsBuilder builder = FamilyDetails.builder()
                .relationshipToDeceased(relationship)
                .grandchildParentAdoptedIn(details.getGrandchildParentAdoptedIn())
                .grandchildParentAdoptionInEnglandOrWales(details.getGrandchildParentAdoptionInEnglandOrWales())
                .grandchildParentAdoptedOut(details.getGrandchildParentAdoptedOut())
                .childAdoptedIn(details.getChildAdoptedIn())
                .childAdoptedOut(details.getChildAdoptedOut())
                .childAdoptionInEnglandOrWales(details.getChildAdoptionInEnglandOrWales())
                .grandchildAdoptedIn(details.getGrandchildAdoptedIn())
                .grandchildAdoptedOut(details.getGrandchildAdoptedOut())
                .grandchildAdoptionInEnglandOrWales(details.getGrandchildAdoptionInEnglandOrWales())
                .deceasedAdoptedIn(details.getDeceasedAdoptedIn())
                .deceasedAdoptedOut(details.getDeceasedAdoptedOut())
                .deceasedAdoptionInEnglandOrWales(details.getDeceasedAdoptionInEnglandOrWales());

        if ("optionGrandchild".equals(relationship)) {
            builder.adoptedIn(details.getGrandchildParentAdoptedIn())
                    .adoptedOut(details.getGrandchildParentAdoptedOut())
                    .adoptionPlace(details.getGrandchildParentAdoptionInEnglandOrWales());
        } else if ("optionChild".equals(relationship)) {
            builder.adoptedIn(details.getChildAdoptedIn())
                    .adoptedOut(details.getChildAdoptedOut())
                    .adoptionPlace(details.getChildAdoptionInEnglandOrWales());
        } else if ("optionParent".equals(relationship)) {
            builder.adoptedIn(details.getDeceasedAdoptedIn())
                    .adoptedOut(details.getDeceasedAdoptedOut())
                    .adoptionPlace(details.getDeceasedAdoptionInEnglandOrWales());
        }


        return builder.build();
    }
}
