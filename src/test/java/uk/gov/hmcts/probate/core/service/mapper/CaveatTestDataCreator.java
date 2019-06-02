package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatApplicant;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatDeceased;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class CaveatTestDataCreator {

    private static final String FIRST_NAME = "firstName";
    private static final String ADDRESS_LINE1 = "address";
    private static final String ADDRESS_LINE2 = "address line 2";
    private static final String ADDRESS_LINE3 = "address line 3";
    private static final String POSTCODE = "postcode";
    private static final String COUNTY = "county";
    private static final String COUNTRY = "country";
    private static final String POSTTOWN = "postTown";

    private static final String EMAIL = "email";
    private static final String LAST_NAME = "lastName";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1967, 12, 3);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 12, 3);
    private static final String DECEASED_FIRST_NAME = "deceasedFirstName";
    private static final String DECEASED_LAST_NAME = "deceasedLastName";
    private static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("250.00");
    private static final long PAYMENT_AMOUNT_LONG = 25000L;
    private static final String PAYMENT_CHANNEL = "payment_channel";
    private static final String PAYMENT_REFERENCE = "payment_reference";
    private static final String PAYMENT_SITE_ID = "payment_siteId";
    private static final String PAYMENT_TRANSACTION_ID = "paymentTransactionId";
    private static final String REGNAME = "Birmingham";
    private static final String ALIAS_FIRST_NAME = "aliasFirstName";
    private static final String ALIAS_LASTNAME = "aliasLastname";
    private static final Date DATE = new Date();
    private static final LocalDate EXPIRYDATE = LocalDate.now().plusDays(30);
    private static final String NAME_0 = "name_0";

    public static CaveatForm createCaveatForm() {
        return CaveatForm.builder()
            .type(ProbateType.CAVEAT)
            .applicant(
                CaveatApplicant.builder()
                    .address(Address.builder()
                            .addressLine1(ADDRESS_LINE1)
                            .addressLine2(ADDRESS_LINE2)
                            .addressLine3(ADDRESS_LINE3)
                            .postTown(POSTTOWN)
                            .postCode(POSTCODE)
                            .county(COUNTY)
                            .country(COUNTRY)
                            .build())
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .build())
            .deceased(CaveatDeceased.builder()
                    .address(Address.builder()
                            .addressLine1(ADDRESS_LINE1)
                            .addressLine2(ADDRESS_LINE2)
                            .addressLine3(ADDRESS_LINE3)
                            .postTown(POSTTOWN)
                            .postCode(POSTCODE)
                            .county(COUNTY)
                            .country(COUNTRY)
                            .build())
                .dateOfBirth(DATE_OF_BIRTH)
                .dateOfDeath(DATE_OF_DEATH)
                .firstName(DECEASED_FIRST_NAME)
                .lastName(DECEASED_LAST_NAME)
                .otherNames(createAliasMap())
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
                        .name(REGNAME)
                        .build())
            .expiryDate(EXPIRYDATE)
            .build();
    }

    private static Map<String, AliasOtherNames> createAliasMap() {
        return ImmutableMap.of(NAME_0, AliasOtherNames.builder()
            .firstName(ALIAS_FIRST_NAME)
            .lastName(ALIAS_LASTNAME)
            .build());
    }

    public static CaveatData createCaveatData() {
        CaveatData caveatData = new CaveatData();
        caveatData.setApplicationType(ApplicationType.PERSONAL);
        caveatData.setExpiryDate(EXPIRYDATE);
        caveatData.setCaveatorEmailAddress(EMAIL);
        caveatData.setCaveatorForenames(FIRST_NAME);
        caveatData.setCaveatorSurname(LAST_NAME);
        uk.gov.hmcts.reform.probate.model.cases.Address caveatorAddress =
                new uk.gov.hmcts.reform.probate.model.cases.Address();
        caveatorAddress.setAddressLine1(ADDRESS_LINE1);
        caveatorAddress.setAddressLine2(ADDRESS_LINE2);
        caveatorAddress.setAddressLine3(ADDRESS_LINE3);
        caveatorAddress.setCounty(COUNTY);
        caveatorAddress.setCountry(COUNTRY);
        caveatorAddress.setPostCode(POSTCODE);
        caveatorAddress.setPostTown(POSTTOWN);
        caveatData.setCaveatorAddress(caveatorAddress);

        caveatData.setDeceasedSurname(DECEASED_LAST_NAME);
        caveatData.setDeceasedForenames(DECEASED_FIRST_NAME);
        caveatData.setDeceasedDateOfBirth(DATE_OF_BIRTH);
        caveatData.setDeceasedDateOfDeath(DATE_OF_DEATH);
        uk.gov.hmcts.reform.probate.model.cases.Address deceasedAddress =
                new uk.gov.hmcts.reform.probate.model.cases.Address();
        deceasedAddress.setAddressLine1(ADDRESS_LINE1);
        deceasedAddress.setAddressLine2(ADDRESS_LINE2);
        deceasedAddress.setAddressLine3(ADDRESS_LINE3);
        deceasedAddress.setCounty(COUNTY);
        deceasedAddress.setCountry(COUNTRY);
        deceasedAddress.setPostCode(POSTCODE);
        deceasedAddress.setPostTown(POSTTOWN);
        caveatData.setDeceasedAddress(deceasedAddress);

        CollectionMember<FullAliasName> aliasNameCollectionMember = new CollectionMember<>();
        FullAliasName aliasName = new FullAliasName();
        aliasName.setFullAliasName(ALIAS_FIRST_NAME + " " + ALIAS_LASTNAME);
        aliasNameCollectionMember.setId(NAME_0);
        aliasNameCollectionMember.setValue(aliasName);
        caveatData.setDeceasedFullAliasNameList(Lists.newArrayList(aliasNameCollectionMember));

        caveatData.setRegistryLocation(RegistryLocation.BIRMINGHAM);

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
        caveatData.setPayments(Lists.newArrayList(paymentCollectionMember));

        caveatData.setCaveatRaisedEmailNotificationRequested(true);
        return caveatData;
    }

    public static CaveatData createInvalidCaveatData() {
        CaveatData caveatData = CaveatTestDataCreator.createCaveatData();
        return caveatData;
    }


    public static CaveatData createPartialCaveatData() {
        CaveatData caveatData = new CaveatData();
        caveatData.setApplicationType(ApplicationType.PERSONAL.PERSONAL);
        caveatData.setCaveatorEmailAddress(EMAIL);
        caveatData.setCaveatorForenames(FIRST_NAME);
        caveatData.setCaveatorSurname(LAST_NAME);
        uk.gov.hmcts.reform.probate.model.cases.Address caveatorAddress =
                new uk.gov.hmcts.reform.probate.model.cases.Address();
        caveatorAddress.setAddressLine1(ADDRESS_LINE1);
        caveatorAddress.setPostCode(POSTCODE);
        uk.gov.hmcts.reform.probate.model.cases.Address deceasedAddress =
                new uk.gov.hmcts.reform.probate.model.cases.Address();
        deceasedAddress.setAddressLine1(ADDRESS_LINE1);
        caveatData.setDeceasedAddress(deceasedAddress);
        return caveatData;
    }
}
