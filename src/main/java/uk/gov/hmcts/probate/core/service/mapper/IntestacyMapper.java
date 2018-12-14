package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.Payment;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Arrays;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class IntestacyMapper implements FormMapper<GrantOfRepresentation, IntestacyForm> {

    @Mappings({
            @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName"),
            @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName"),
            @Mapping(target = "primaryApplicantRelationshipToDeceased", source = "applicant.relationshipToDeceased"),
            @Mapping(target = "primaryApplicantAdoptionInEnglandOrWales", source = "applicant.adoptionInEnglandOrWales"),
            @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber"),
            @Mapping(target = "primaryApplicantEmailAddress", source = "applicant.email"),
            @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound"),
            @Mapping(target = "deceasedSurname", source = "deceased.lastName"),
            @Mapping(target = "deceasedForenames", source = "deceased.firstName"),
            @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth"),
            @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath"),
            @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound"),
            @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias"),
            @Mapping(target = "deceasedMaritalStatus", source = "deceased.maritalStatus"),
            @Mapping(target = "deceasedDivorcedInEnglandOrWales", source = "deceased.divorcedInEnglandOrWales"),
            @Mapping(target = "deceasedDomicileInEngWales", source = "deceased.domiciledInEnglandOrWales"),
            @Mapping(target = "deceasedOtherChildren", source = "deceased.otherChildren"),
            @Mapping(target = "deceasedAllDeceasedChildrenOverEighteen", source = "deceased.allDeceasedChildrenOverEighteen"),
            @Mapping(target = "deceasedAnyDeceasedChildrenDieBeforeDeceased", source = "deceased.anyDeceasedChildrenDieBeforeDeceased"),
            @Mapping(target = "deceasedAnyDeceasedGrandchildrenUnderEighteen", source = "deceased.anyDeceasedGrandchildrenUnderEighteen"),
            @Mapping(target = "deceasedSpouseNotApplyingReason", source = "deceased.spouseNotApplyingReason"),
            @Mapping(target = "deceasedAnyChildren", source = "deceased.anyChildren"),
            @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas"),
            @Mapping(target = "extraCopiesOfGrant", source = "copies.uk"),
            @Mapping(target = "assetsOverseas", source = "assets.assetsOverseas"),
            @Mapping(target = "assetsOverseasNetValue", source = "assets.assetsOverseasNetValue"),
            @Mapping(target = "ihtReferenceNumber", source = "iht.identifier"),
            @Mapping(target = "registryLocation", source = "registry.address")
    })
    public abstract GrantOfRepresentation map(IntestacyForm form);

    @AfterMapping
    public void mapProbateType(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        grantOfRepresentation.setApplicationType(ProbateType.INTESTACY);
    }

    @AfterMapping
    public void mapIhtValues(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        grantOfRepresentation.setIhtFormCompletedOnline(form.getIht().getMethod().equals(IhtMethod.ONLINE));
        grantOfRepresentation.setIhtFormId(form.getIht().getForm());
        grantOfRepresentation.setIhtNetValue(form.getIht().getNetValue().multiply(new BigDecimal(100)).longValue());
        grantOfRepresentation.setIhtGrossValue(form.getIht().getGrossValue().multiply(new BigDecimal(100)).longValue());
    }
    @AfterMapping
    public void mapAssetsValues(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        grantOfRepresentation.setAssetsOverseasNetValue(form.getAssets()
                .getAssetsOverseasNetValue().multiply(new BigDecimal(100)).longValue());
    }

    @AfterMapping
    public void mapAddresses(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        grantOfRepresentation.setPrimaryApplicantAddress(createAddress(form.getApplicant().getAddress(), form.getApplicant().getPostCode()));
        grantOfRepresentation.setDeceasedAddress(createAddress(form.getDeceased().getAddress(), null));
    }

    @AfterMapping
    public void mapDeclaration(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        Declaration declaration = new Declaration();
        declaration.setDeclarationCheckbox(form.getDeclaration().getDeclarationAgreement());
        grantOfRepresentation.setDeclaration(declaration);
    }

    @AfterMapping
    public void mapPayment(IntestacyForm form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {

        Payment payment = new Payment();
        uk.gov.hmcts.reform.probate.model.forms.Payment paymentForm = form.getPayment();
        payment.setDate(paymentForm.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        payment.setAmount(paymentForm.getAmount()
                .multiply(new BigDecimal(100)).longValue());
        payment.setMethod(paymentForm.getChannel());
        payment.setReference(paymentForm.getReference());
        payment.setStatus(paymentForm.getStatus());
        payment.setTransactionId(paymentForm.getTransactionId());
        CollectionMember<Payment> paymentCollectionMember = new CollectionMember<>();
        paymentCollectionMember.setValue(payment);

        grantOfRepresentation.setPayments(Arrays.asList(paymentCollectionMember));
    }

    private Address createAddress(String addressStr, String postCode) {
        Address address = new Address();
        address.setAddressLine1(addressStr);
        address.setPostCode(postCode);
        return address;
    }

}

