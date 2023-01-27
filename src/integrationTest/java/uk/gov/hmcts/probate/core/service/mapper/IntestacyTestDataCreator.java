package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import uk.gov.hmcts.reform.probate.model.IhtFormEstate;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.DeathCertificate;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static java.lang.Boolean.TRUE;

public final class IntestacyTestDataCreator {

    public static final String UPLOAD_DOCUMENT_URL = "http://document-management/document/12345";
    public static final String DEATH_CERT = "optionDeathCertificate";
    private static final String FIRST_NAME = "firstName";
    private static final uk.gov.hmcts.reform.probate.model.forms.Address ADDRESS =
        uk.gov.hmcts.reform.probate.model.forms.Address.builder()
            .addressLine1("address").build();
    private static final Address CASE_ADDRESS = Address.builder()
        .addressLine1("address").build();
    private static final String EMAIL = "email";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String POSTCODE = "postcode";
    private static final BigDecimal ASSETS_OVERSEAS_NET_VALUE = new BigDecimal("1000.00");
    private static final Long ASSETS_OVERSEAS_NET_VALUE_LONG = 100000L;
    private static final long COPIES_OVERSEAS = 5L;
    private static final long COPIES_UK = 100000L;
    private static final uk.gov.hmcts.reform.probate.model.forms.Address DECEASED_ADDRESS =
        uk.gov.hmcts.reform.probate.model.forms.Address.builder().addressLine1("deceasedAddress").build();
    private static final Address CASE_DECEASED_ADDRESS = Address.builder()
        .addressLine1("deceasedAddress").build();
    private static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.of(1967, 12, 3, 0, 0, 0);
    private static final LocalDateTime DATE_OF_DEATH = LocalDateTime.of(2018, 12, 3, 0, 0, 0);
    private static final String DECEASED_FIRST_NAME = "deceasedFirstName";
    private static final String DECEASED_LAST_NAME = "deceasedLastName";
    private static final BigDecimal GROSS_VALUE = new BigDecimal("100000.00");
    private static final Long GROSS_VALUE_LONG = 10000000L;
    private static final String IHT_IDENTIFIER = "ihtIdentifier";
    private static final BigDecimal NET_VALUE = new BigDecimal("90000.00");
    private static final Long NET_VALUE_LONG = 9000000L;
    private static final BigDecimal ESTATE_GROSS_VALUE = new BigDecimal("30000.00");
    private static final String ESTATE_GROSS_VALUE_FIELD = "30000";
    private static final BigDecimal ESTATE_NET_VALUE = new BigDecimal("20000.00");
    private static final String ESTATE_NET_VALUE_FIELD = "20000";
    private static final BigDecimal ESTATE_NET_QUALIFYING_VALUE = new BigDecimal("10000.00");
    private static final String ESTATE_NET_QUALIFYING_VALUE_FIELD = "10000";
    private static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("250.00");
    private static final long PAYMENT_AMOUNT_LONG = 25000L;
    private static final String PAYMENT_CHANNEL = "payment_channel";
    private static final String PAYMENT_REFERENCE = "payment_reference";
    private static final String PAYMENT_SITE_ID = "payment_siteId";
    private static final String PAYMENT_TRANSACTION_ID = "paymentTransactionId";
    private static final String REG_ADDRESS = "regAddress";
    private static final String REG_EMAIL = "regEmail";
    private static final String REGNAME = "Birmingham";
    private static final long SEQUENCE_NUMBER = 1L;
    private static final String ALIAS_FIRST_NAME = "aliasFirstName";
    private static final String ALIAS_LASTNAME = "aliasLastname";
    private static final Date DATE = new Date();

    private IntestacyTestDataCreator() {

    }

    private static Map<String, AliasOtherNames> createAliasMap() {
        return ImmutableMap.of("name_0", AliasOtherNames.builder()
            .firstName(ALIAS_FIRST_NAME)
            .lastName(ALIAS_LASTNAME)
            .build());
    }

    public static IntestacyForm createIntestacyForm() {
        return IntestacyForm.builder()
            .applicantEmail(EMAIL)
            .type(ProbateType.INTESTACY)
            .caseType(GrantType.INTESTACY.getName())
            .language(Language.builder().bilingual(Boolean.TRUE).build())
            .applicant(
                IntestacyApplicant.builder()
                    .address(ADDRESS)
                    .addressFound(Boolean.TRUE)
                    .adoptionInEnglandOrWales(Boolean.TRUE)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .phoneNumber(PHONE_NUMBER)
                    .postcode(POSTCODE)
                    .relationshipToDeceased(Relationship.ADOPTED_CHILD.getDescription())
                    .spouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE.getDescription())
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
                .lastName(DECEASED_LAST_NAME)
                .maritalStatus(MaritalStatus.MARRIED.getDescription())
                .anyDeceasedGrandchildrenUnderEighteen(Boolean.FALSE)
                .otherChildren(Boolean.FALSE)
                .otherNames(createAliasMap())
                .postcode(POSTCODE)
                .diedEngOrWales(Boolean.TRUE)
                .deathCertificate(DEATH_CERT)
                .englishForeignDeathCert(Boolean.FALSE)
                .foreignDeathCertTranslation(Boolean.TRUE)
                .build())
            .declaration(Declaration.builder().build())
            .iht(InheritanceTax.builder()
                .form(IhtFormType.optionIHT205.toString())
                .grossValue(GROSS_VALUE)
                .identifier(IHT_IDENTIFIER)
                .method(IhtMethod.BY_POST)
                .netValue(NET_VALUE)
                .assetsOutside(Boolean.TRUE)
                .assetsOutsideNetValue(ASSETS_OVERSEAS_NET_VALUE)
                .estateValueCompleted(TRUE)
                .ihtFormEstateId(IhtFormType.optionIHT400421.name())
                .estateGrossValue(ESTATE_GROSS_VALUE)
                .estateGrossValueField(ESTATE_GROSS_VALUE_FIELD)
                .estateNetValue(ESTATE_NET_VALUE)
                .estateNetValueField(ESTATE_NET_VALUE_FIELD)
                .estateNetQualifyingValue(ESTATE_NET_QUALIFYING_VALUE)
                .estateNetQualifyingValueField(ESTATE_NET_QUALIFYING_VALUE_FIELD)
                .deceasedHadLateSpouseOrCivilPartner(TRUE)
                .unusedAllowanceClaimed(TRUE)
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

    public static GrantOfRepresentationData createGrantOfRepresentation() {
        GrantOfRepresentationData grantOfRepresentation = new GrantOfRepresentationData();
        grantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        grantOfRepresentation.setGrantType(GrantType.INTESTACY);
        grantOfRepresentation.setPrimaryApplicantEmailAddress(EMAIL);
        grantOfRepresentation.setPrimaryApplicantForenames(FIRST_NAME);
        grantOfRepresentation.setPrimaryApplicantSurname(LAST_NAME);
        Address primaryApplicantAddress = CASE_ADDRESS;
        grantOfRepresentation.setPrimaryApplicantAddress(primaryApplicantAddress);
        grantOfRepresentation.setPrimaryApplicantAddressFound(Boolean.TRUE);
        grantOfRepresentation.setPrimaryApplicantPostCode(POSTCODE);
        grantOfRepresentation.setPrimaryApplicantPhoneNumber(PHONE_NUMBER);
        grantOfRepresentation.setPrimaryApplicantRelationshipToDeceased(Relationship.ADOPTED_CHILD);
        grantOfRepresentation.setPrimaryApplicantAdoptionInEnglandOrWales(Boolean.TRUE);
        grantOfRepresentation.setLanguagePreferenceWelsh(Boolean.TRUE);

        grantOfRepresentation.setDeceasedSpouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE);
        grantOfRepresentation.setDeceasedSurname(DECEASED_LAST_NAME);
        grantOfRepresentation.setDeceasedForenames(DECEASED_FIRST_NAME);
        grantOfRepresentation.setDeceasedDateOfBirth(DATE_OF_BIRTH.toLocalDate());
        grantOfRepresentation.setDeceasedDateOfDeath(DATE_OF_DEATH.toLocalDate());
        Address deceasedAddress = CASE_DECEASED_ADDRESS;
        grantOfRepresentation.setDeceasedAddress(deceasedAddress);
        grantOfRepresentation.setDeceasedPostCode(POSTCODE);
        grantOfRepresentation.setDeceasedAddressFound(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        grantOfRepresentation.setDeceasedForeignDeathCertInEnglish(Boolean.FALSE);
        grantOfRepresentation.setDeceasedForeignDeathCertTranslation(Boolean.TRUE);
        CollectionMember<AliasName> aliasNameCollectionMember = new CollectionMember<>();
        AliasName aliasName = new AliasName();
        aliasName.setForenames(ALIAS_FIRST_NAME);
        aliasName.setLastName(ALIAS_LASTNAME);
        aliasNameCollectionMember.setValue(aliasName);
        grantOfRepresentation.setDeceasedDomicileInEngWales(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAliasNameList(Lists.newArrayList(aliasNameCollectionMember));
        grantOfRepresentation.setDeceasedMaritalStatus(MaritalStatus.MARRIED);
        grantOfRepresentation.setDeceasedDivorcedInEnglandOrWales(Boolean.FALSE);
        grantOfRepresentation.setDeceasedOtherChildren(Boolean.FALSE);
        grantOfRepresentation.setChildrenDied(Boolean.FALSE);
        grantOfRepresentation.setGrandChildrenSurvivedUnderEighteen(Boolean.FALSE);
        grantOfRepresentation.setChildrenOverEighteenSurvived(Boolean.FALSE);
        grantOfRepresentation.setDeceasedAnyChildren(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        grantOfRepresentation.setDeceasedDiedEngOrWales(Boolean.TRUE);
        grantOfRepresentation.setDeceasedDeathCertificate(DeathCertificate.DEATH_CERTIFICATE);

        grantOfRepresentation.setDeclaration(
            uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.builder().build());

        grantOfRepresentation.setRegistryLocation(RegistryLocation.BIRMINGHAM);

        grantOfRepresentation.setDeceasedHasAssetsOutsideUK(Boolean.TRUE);
        grantOfRepresentation.setAssetsOutsideNetValue(ASSETS_OVERSEAS_NET_VALUE_LONG);
        grantOfRepresentation.setIhtFormId(IhtFormType.optionIHT205);
        grantOfRepresentation.setIhtFormCompletedOnline(Boolean.FALSE);
        grantOfRepresentation.setIhtGrossValue(GROSS_VALUE_LONG);
        grantOfRepresentation.setIhtNetValue(NET_VALUE_LONG);
        grantOfRepresentation.setIhtReferenceNumber(IHT_IDENTIFIER);
        grantOfRepresentation.setIhtFormEstateValuesCompleted(TRUE);
        grantOfRepresentation.setIhtFormEstate(IhtFormEstate.optionIHT400421);
        grantOfRepresentation.setIhtEstateGrossValue(ESTATE_GROSS_VALUE.multiply(BigDecimal.valueOf(100)).longValue());
        grantOfRepresentation.setIhtEstateGrossValueField(ESTATE_GROSS_VALUE_FIELD);
        grantOfRepresentation.setIhtEstateNetValue(ESTATE_NET_VALUE.multiply(BigDecimal.valueOf(100)).longValue());
        grantOfRepresentation.setIhtEstateNetValueField(ESTATE_NET_VALUE_FIELD);
        grantOfRepresentation.setIhtEstateNetQualifyingValue(ESTATE_NET_QUALIFYING_VALUE
            .multiply(BigDecimal.valueOf(100)).longValue());
        grantOfRepresentation.setIhtEstateNetQualifyingValueField(ESTATE_NET_QUALIFYING_VALUE_FIELD);
        grantOfRepresentation.setDeceasedHadLateSpouseOrCivilPartner(TRUE);
        grantOfRepresentation.setIhtUnusedAllowanceClaimed(TRUE);

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
        return grantOfRepresentation;
    }

    public static GrantOfRepresentationData createInvalidGrantOfRepresentation() {
        GrantOfRepresentationData grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
        grantOfRepresentation.setIhtNetValue(200000000L);
        return grantOfRepresentation;
    }


    public static GrantOfRepresentationData createPartialGrantOfRepresentation() {
        GrantOfRepresentationData grantOfRepresentation = new GrantOfRepresentationData();
        grantOfRepresentation.setApplicationType(ApplicationType.PERSONAL.PERSONAL);
        grantOfRepresentation.setPrimaryApplicantEmailAddress(EMAIL);
        grantOfRepresentation.setPrimaryApplicantForenames(FIRST_NAME);
        grantOfRepresentation.setPrimaryApplicantSurname(LAST_NAME);
        Address primaryApplicantAddress = CASE_ADDRESS;
        grantOfRepresentation.setPrimaryApplicantAddress(primaryApplicantAddress);
        Address deceasedAddress = CASE_DECEASED_ADDRESS;
        grantOfRepresentation.setDeceasedAddress(deceasedAddress);
        grantOfRepresentation.setDeceasedAddressFound(Boolean.TRUE);
        grantOfRepresentation.setDeceasedAnyOtherNames(Boolean.TRUE);
        return grantOfRepresentation;
    }
}
