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
                .build();
    }

    @FromApplicantFamilyDetails
    public FamilyDetails fromApplicantDetails(ApplicantFamilyDetails details) {
        if (details == null) {
            return null;
        }
        return FamilyDetails.builder()
                .relationshipToDeceased(details.getRelationshipToDeceased().getDescription())
                .childAdoptedIn(details.getChildAdoptedIn())
                .childAdoptionInEnglandOrWales(details.getChildAdoptionInEnglandOrWales())
                .childAdoptedOut(details.getChildAdoptedOut())
                .grandchildAdoptedIn(details.getGrandchildAdoptedIn())
                .grandchildAdoptionInEnglandOrWales(details.getGrandchildAdoptionInEnglandOrWales())
                .grandchildAdoptedOut(details.getGrandchildAdoptedOut())
                .grandchildParentAdoptedIn(details.getGrandchildParentAdoptedIn())
                .grandchildParentAdoptionInEnglandOrWales(details.getGrandchildParentAdoptionInEnglandOrWales())
                .grandchildParentAdoptedOut(details.getGrandchildParentAdoptedOut())
                .build();
    }
}
