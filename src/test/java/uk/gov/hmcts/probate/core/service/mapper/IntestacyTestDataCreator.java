package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyAssets;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class IntestacyTestDataCreator {

    private static final String FIRST_NAME = "firstName";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    private static final String FREE_TEXT_ADDRESS = "freeTextAddress";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String POSTCODE = "postcode";
    private static final BigDecimal ASSETS_OVERSEAS_NET_VALUE = new BigDecimal("1000.00");
    private static final Long ASSETS_OVERSEAS_NET_VALUE_LONG = 100000L;
    private static final long COPIES_OVERSEAS = 5l;
    private static final long COPIES_UK = 100000L;
    private static final String DECEASED_ADDRESS = "deceasedAddress";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1967, 12, 3);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 12, 3);
    private static final String DECEASED_FIRST_NAME = "deceasedFirstName";
    private static final String DECEASED_FREE_TEXT_ADDRESS = "deceasedFreeTextAddress";
    private static final String DECEASED_LAST_NAME = "deceasedLastName";
    //private static final long CASE_ID = 100001L;
    //private static final String STATE = "state";
    private static final BigDecimal GROSS_VALUE = new BigDecimal("100000.00");
    private static final Long GROSS_VALUE_LONG = 10000000L;
    private static final String IHT_IDENTIFIER = "ihtIdentifier";
    private static final BigDecimal NET_VALUE = new BigDecimal("90000.00");
    private static final Long NET_VALUE_LONG = 9000000L;
    private static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("250.00");
    private static final long PAYMENT_AMOUNT_LONG = 25000L;
    private static final String PAYMENT_CHANNEL = "payment_channel";
    private static final String PAYMENT_REFERENCE = "payment_reference";
    private static final String PAYMENT_SITE_ID = "payment_siteId";
    private static final String PAYMENT_TRANSACTION_ID = "paymentTransactionId";
    private static final String REG_ADDRESS = "regAddress";
    private static final String REG_EMAIL = "regEmail";
    private static final String REGNAME = "regname";
    private static final long SEQUENCE_NUMBER = 1L;
    private static final String ALIAS_FIRST_NAME = "aliasFirstName";
    private static final String ALIAS_LASTNAME = "aliasLastname";
    private static final Date DATE = new Date();
    public static final String UPLOAD_DOCUMENT_URL = "http://document-management/document/12345";

    public static IntestacyForm createIntestacyForm() {
        return IntestacyForm.builder()
            .type(ProbateType.INTESTACY)
            .uploadDocumentUrl(UPLOAD_DOCUMENT_URL)
            .applicant(
                IntestacyApplicant.builder()
                    .address(ADDRESS)
                    .addressFound(Boolean.TRUE)
                    .adoptionInEnglandOrWales(Boolean.TRUE)
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .freeTextAddress(FREE_TEXT_ADDRESS)
                    .lastName(LAST_NAME)
                    .phoneNumber(PHONE_NUMBER)
                    .postCode(POSTCODE)
                    .relationshipToDeceased(Relationship.ADOPTED_CHILD)
                    .build())
            .assets(
                IntestacyAssets.builder()
                    .assetsOverseas(Boolean.TRUE)
                    .assetsOverseasNetValue(ASSETS_OVERSEAS_NET_VALUE)
                    .build())
            .copies(Copies.builder()
                .overseas(COPIES_OVERSEAS)
                .uk(COPIES_UK)
                .build())
            .deceased(IntestacyDeceased.builder()
                .anyDeceasedChildrenDieBeforeDeceased(Boolean.FALSE)
                .address(DECEASED_ADDRESS)
                .addressFound(Boolean.TRUE)
                .alias(Boolean.TRUE)
                .allDeceasedChildrenOverEighteen(Boolean.FALSE)
                .anyChildren(Boolean.TRUE)
                .dateOfBirth(DATE_OF_BIRTH)
                .dateOfDeath(DATE_OF_DEATH)
                .divorcedInEnglandOrWales(Boolean.FALSE)
                .domiciledInEnglandOrWales(Boolean.TRUE)
                .firstName(DECEASED_FIRST_NAME)
                .freeTextAddress(DECEASED_FREE_TEXT_ADDRESS)
                .lastName(DECEASED_LAST_NAME)
                .maritalStatus(MaritalStatus.MARRIED)
                .anyDeceasedGrandchildrenUnderEighteen(Boolean.FALSE)
                .otherChildren(Boolean.FALSE)
                .otherNames(createAliasMap())
                .spouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE)
                .build())
            .declaration(IntestacyDeclaration.builder()
                .declarationAgreement(Boolean.FALSE)
                .build())
            .iht(InheritanceTax.builder()
                .form(IhtFormType.IHT205)
                .grossValue(GROSS_VALUE)
                .identifier(IHT_IDENTIFIER)
                .method(IhtMethod.BY_POST)
                .netValue(NET_VALUE)
                .build())
                .payments(Lists.newArrayList(Payment.builder()
                        .amount(PAYMENT_AMOUNT)
                        .method(PAYMENT_CHANNEL)
                        .date(DATE)
                        .reference(PAYMENT_REFERENCE)
                        .siteId(PAYMENT_SITE_ID)
                        .status(PaymentStatus.SUCCESS)
                        .transactionId(PAYMENT_TRANSACTION_ID)
                        .build()))
                .registry(Registry.builder()
                        .address(REG_ADDRESS)
                        .email(REG_EMAIL)
                        .name(REGNAME)
                        .sequenceNumber(SEQUENCE_NUMBER)
                        .build())
                .build();
    }

    private static Map<String, AliasOtherNames> createAliasMap() {
        return ImmutableMap.of("name_0", AliasOtherNames.builder()
            .firstName(ALIAS_FIRST_NAME)
            .lastName(ALIAS_LASTNAME)
            .build());
    }

    public static GrantOfRepresentation createGrantOfRepresentation() {
        GrantOfRepresentation grantOfRepresentation = new GrantOfRepresentation();
        grantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        grantOfRepresentation.setCaseType(GrantType.INTESTACY);
        grantOfRepresentation.setPrimaryApplicantEmailAddress(EMAIL);
        grantOfRepresentation.setPrimaryApplicantForenames(FIRST_NAME);
        grantOfRepresentation.setPrimaryApplicantSurname(LAST_NAME);
        Address primaryApplicantAddress = new Address();
        primaryApplicantAddress.setAddressLine1(ADDRESS);
        primaryApplicantAddress.setPostCode(POSTCODE);
        grantOfRepresentation.setPrimaryApplicantAddress(primaryApplicantAddress);
        grantOfRepresentation.setPrimaryApplicantAddressFound(Boolean.TRUE);
        grantOfRepresentation.setPrimaryApplicantFreeTextAddress(FREE_TEXT_ADDRESS);
        grantOfRepresentation.setPrimaryApplicantPhoneNumber(PHONE_NUMBER);
        grantOfRepresentation.setPrimaryApplicantRelationshipToDeceased(Relationship.ADOPTED_CHILD);
        grantOfRepresentation.setPrimaryApplicantAdoptionInEnglandOrWales(Boolean.TRUE);

        grantOfRepresentation.setDeceasedSpouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE);
        grantOfRepresentation.setDeceasedSurname(DECEASED_LAST_NAME);
        grantOfRepresentation.setDeceasedForenames(DECEASED_FIRST_NAME);
        grantOfRepresentation.setDeceasedDateOfBirth(DATE_OF_BIRTH);
        grantOfRepresentation.setDeceasedDateOfDeath(DATE_OF_DEATH);
        Address deceasedAddress = new Address();
        deceasedAddress.setAddressLine1(DECEASED_ADDRESS);
        grantOfRepresentation.setDeceasedAddress(deceasedAddress);
        grantOfRepresentation.setDeceasedFreeTextAddress(DECEASED_FREE_TEXT_ADDRESS);
        grantOfRepresentation.setDeceasedAddressFound(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        CollectionMember<AliasName> aliasNameCollectionMember = new CollectionMember<>();
        AliasName aliasName = new AliasName();
        aliasName.setForenames(ALIAS_FIRST_NAME);
        aliasName.setLastName(ALIAS_LASTNAME);
        aliasNameCollectionMember.setValue(aliasName);
        grantOfRepresentation.setDeceasedDomicileInEngWales(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAliasNameList(Lists.newArrayList(aliasNameCollectionMember));
        grantOfRepresentation.setDeceasedMartialStatus(MaritalStatus.MARRIED);
        grantOfRepresentation.setDeceasedDivorcedInEnglandOrWales(Boolean.FALSE);
        grantOfRepresentation.setDeceasedOtherChildren(Boolean.FALSE);
        grantOfRepresentation.setChildrenDied(Boolean.FALSE);
        grantOfRepresentation.setGrandChildrenSurvivedUnderEighteen(Boolean.FALSE);
        grantOfRepresentation.setChildrenOverEighteenSurvived(Boolean.FALSE);
        grantOfRepresentation.setDeceasedAnyChildren(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        grantOfRepresentation.setDeceasedFreeTextAddress(DECEASED_FREE_TEXT_ADDRESS);

        grantOfRepresentation.setRegistryLocation(REGNAME);
        grantOfRepresentation.setRegistryAddress(REG_ADDRESS);
        grantOfRepresentation.setRegistryEmail(REG_EMAIL);
        grantOfRepresentation.setRegistrySequenceNumber(Long.toString(SEQUENCE_NUMBER));

        grantOfRepresentation.setDeceasedHasAssetsOutsideUK(Boolean.TRUE);
        grantOfRepresentation.setAssetsOverseasNetValue(ASSETS_OVERSEAS_NET_VALUE_LONG);
        grantOfRepresentation.setIhtFormId(IhtFormType.IHT205);
        grantOfRepresentation.setIhtFormCompletedOnline(Boolean.FALSE);
        grantOfRepresentation.setIhtGrossValue(GROSS_VALUE_LONG);
        grantOfRepresentation.setIhtNetValue(NET_VALUE_LONG);
        grantOfRepresentation.setIhtReferenceNumber(IHT_IDENTIFIER);

        Declaration declaration = new Declaration();
        declaration.setDeclarationCheckbox(Boolean.FALSE);
        grantOfRepresentation.setDeclaration(declaration);

        grantOfRepresentation.setExtraCopiesOfGrant(COPIES_UK);
        grantOfRepresentation.setOutsideUkGrantCopies(COPIES_OVERSEAS);

        final CollectionMember<CasePayment> paymentCollectionMember = new CollectionMember<>();
        CasePayment payment = new CasePayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setDate(DATE);
        payment.setReference(PAYMENT_REFERENCE);
        payment.setAmount(PAYMENT_AMOUNT_LONG);
        payment.setMethod(PAYMENT_CHANNEL);
        payment.setTransactionId(PAYMENT_TRANSACTION_ID);
        payment.setSiteId(PAYMENT_SITE_ID);
        paymentCollectionMember.setValue(payment);
        grantOfRepresentation.setPayments(Lists.newArrayList(paymentCollectionMember));
        grantOfRepresentation.setUploadDocumentUrl(UPLOAD_DOCUMENT_URL);
        return grantOfRepresentation;
    }

    public static GrantOfRepresentation createInvalidGrantOfRepresentation() {
        GrantOfRepresentation grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
        grantOfRepresentation.setIhtNetValue(200000000L);
        return grantOfRepresentation;
    }


    public static GrantOfRepresentation createPartialGrantOfRepresentation() {
        GrantOfRepresentation grantOfRepresentation = new GrantOfRepresentation();
        grantOfRepresentation.setApplicationType(ApplicationType.PERSONAL.PERSONAL);
        grantOfRepresentation.setPrimaryApplicantEmailAddress(EMAIL);
        grantOfRepresentation.setPrimaryApplicantForenames(FIRST_NAME);
        grantOfRepresentation.setPrimaryApplicantSurname(LAST_NAME);
        Address primaryApplicantAddress = new Address();
        primaryApplicantAddress.setAddressLine1(ADDRESS);
        primaryApplicantAddress.setPostCode(POSTCODE);
        Address deceasedAddress = new Address();
        deceasedAddress.setAddressLine1(DECEASED_ADDRESS);
        grantOfRepresentation.setDeceasedAddress(deceasedAddress);
        grantOfRepresentation.setDeceasedFreeTextAddress(DECEASED_FREE_TEXT_ADDRESS);
        grantOfRepresentation.setDeceasedAddressFound(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        return grantOfRepresentation;
    }
}
