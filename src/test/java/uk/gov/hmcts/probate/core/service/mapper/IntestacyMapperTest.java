package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.Payment;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.AliasOtherNamesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.CcdCaseBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.CopiesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.InheritanceTaxBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.IntestacyApplicantBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.IntestacyAssetsBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.IntestacyDeceasedBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.IntestacyDeclarationBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.IntestacyFormBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.PaymentBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.builders.RegistryBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class IntestacyMapperTest {


    public static final String FIRST_NAME = "firstName";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String FREE_TEXT_ADDRESS = "freeTextAddress";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String POSTCODE = "postcode";
    public static final BigDecimal ASSETS_OVERSEAS_NET_VALUE = new BigDecimal("1000.00");
    public static final Long ASSETS_OVERSEAS_NET_VALUE_LONG =  100000L;
    public static final long COPIES_OVERSEAS = 5l;
    public static final long COPIES_UK = 100000L;
    public static final String DECEASED_ADDRESS = "deceasedAddress";
    public static final LocalDate DATE_OF_BIRTH = LocalDate.of(1967, 12, 3);
    public static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 12, 3);
    public static final String DECEASED_FIRST_NAME = "deceasedFirstName";
    public static final String DECEASED_FREE_TEXT_ADDRESS = "deceasedFreeTextAddress";
    public static final String DECEASED_LAST_NAME = "deceasedLastName";
    public static final long CASE_ID = 100001L;
    public static final String STATE = "state";
    public static final BigDecimal GROSS_VALUE = new BigDecimal("100000.00");
    public static final Long GROSS_VALUE_LONG =10000000L;
    public static final String IHT_IDENTIFIER = "ihtIdentifier";
    public static final BigDecimal NET_VALUE = new BigDecimal("90000.00");
    public static final Long NET_VALUE_LONG =9000000L;
    public static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("250.00");
    public static final long PAYMENT_AMOUNT_LONG = 25000L;
    public static final String PAYMENT_CHANNEL = "payment_channel";
    public static final String PAYMENT_REFERENCE = "payment_reference";
    public static final String PAYMENT_SITE_ID = "payment_siteId";
    public static final String PAYMENT_TRANSACTION_ID = "paymentTransactionId";
    public static final String REG_ADDRESS = "regAddress";
    public static final String REG_EMAIL = "regEmail";
    public static final String REGNAME = "regname";
    public static final long SEQUENCE_NUMBER = 1L;
    public static final String ALIAS_FIRST_NAME = "aliasFirstName";
    public static final String ALIAS_LASTNAME = "aliasLastname";

    IntestacyMapper mapper =Mappers.getMapper(IntestacyMapper.class);
    IntestacyForm intestacyForm;

    @BeforeEach
    void setUp() throws IOException {
        //FileReader fileReader = new FileReader("../test/resources/formData.json");
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource("formData.json").getFile());

         intestacyForm = IntestacyFormBuilder.anIntestacyForm()
                .withType(ProbateType.INTESTACY)
                .withApplicant(
                        IntestacyApplicantBuilder.anIntestacyApplicant()
                        .withAddress(ADDRESS)
                        .withAddressFound(Boolean.TRUE)
                        .withAdoptionInEnglandOrWales(Boolean.TRUE)
                        .withEmail(EMAIL)
                        .withFirstName(FIRST_NAME)
                        .withFreeTextAddress(FREE_TEXT_ADDRESS)
                        .withLastName(LAST_NAME)
                        .withPhoneNumber(PHONE_NUMBER)
                        .withPostCode(POSTCODE)
                        .withRelationshipToDeceased(Relationship.CHILD)
                        .build())
                .withAssets(
                        IntestacyAssetsBuilder.anIntestacyAssets()
                         .withAssetsOverseas(Boolean.TRUE)
                         .withAssetsOverseasNetValue(ASSETS_OVERSEAS_NET_VALUE)
                        .build())
                .withCopies(CopiesBuilder.createCopies()
                        .withOverseas(COPIES_OVERSEAS)
                        .withUk(COPIES_UK)
                        .build())
                .withDeceased(IntestacyDeceasedBuilder.anIntestacyDeceased()
                        .withAnyDeceasedChildrenDieBeforeDeceased(Boolean.FALSE)
                        .withAddress(DECEASED_ADDRESS)
                        .withAddressFound(Boolean.TRUE)
                        .withAlias(Boolean.TRUE)
                        .withAllDeceasedChildrenOverEighteen(Boolean.FALSE)
                        .withAnyChildren(Boolean.TRUE)
                        .withDateOfBirth(DATE_OF_BIRTH)
                        .withDateOfDeath(DATE_OF_DEATH)
                        .withDivorcedInEnglandOrWales(Boolean.FALSE)
                        .withDomiciledInEnglandOrWales(Boolean.TRUE)
                        .withFirstName(DECEASED_FIRST_NAME)
                        .withFreeTextAddress(DECEASED_FREE_TEXT_ADDRESS)
                        .withLastName(DECEASED_LAST_NAME)
                        .withMaritalStatus(MaritalStatus.MARRIED)
                        .withAnyDeceasedGrandchildrenUnderEighteen(Boolean.FALSE)
                        .withOtherChildren(Boolean.FALSE)
                        .withOtherNames(createAliasMap())
                        .withSpouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE)
                        .build())
                .withDeclaration(IntestacyDeclarationBuilder.anIntestacyDeclaration()
                        .withDeclarationAgreement(Boolean.FALSE)
                        .build())
                .withCcdCase(CcdCaseBuilder.createCcdCase()
                        .withId(CASE_ID)
                        .withState(STATE)
                        .build())
                .withIht(InheritanceTaxBuilder.anInheritanceTax()
                        .withForm(IhtFormType.IHT205)
                        .withGrossValue(GROSS_VALUE)
                        .withIdentifier(IHT_IDENTIFIER)
                        .withMethod(IhtMethod.BY_POST)
                        .withNetValue(NET_VALUE)
                        .build())
                .withPayment(PaymentBuilder.createPayment()
                        .withAmount(PAYMENT_AMOUNT)
                        .withChannel(PAYMENT_CHANNEL)
                        .withDate(getFixedDate())
                        .withReference(PAYMENT_REFERENCE)
                        .withSiteId(PAYMENT_SITE_ID)
                        .withStatus(PaymentStatus.SUCCESS)
                        .withTransactionId(PAYMENT_TRANSACTION_ID)
                    .build())
                .withRegistry(RegistryBuilder.createRegistry()
                        .withAddress(REG_ADDRESS)
                        .withEmail(REG_EMAIL)
                        .withName(REGNAME)
                        .withSequenceNumber(SEQUENCE_NUMBER)
                        .build())
                .build();

        //IntestacyForm form = new ObjectMapper().readValue(fileReader, IntestacyForm.class);
//        IntestacyForm form = new ObjectMapper().readValue(file, IntestacyForm.class);
    }

    private Map<String, AliasOtherNames> createAliasMap() {
        HashMap<String, AliasOtherNames> aliasMap = new HashMap<>();
        aliasMap.put("key", AliasOtherNamesBuilder.anAliasOtherNames()
                                    .withFirstName(ALIAS_FIRST_NAME)
                                    .withLastName(ALIAS_LASTNAME)
                .build());

        return aliasMap;
    }

    private Date getFixedDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 12, 23);
        return calendar.getTime();
    }

    @Test
    void shouldMapIntestacyFormToGrantOfRepresentation() {

        GrantOfRepresentation grantOfRepresentation  = mapper.map(intestacyForm);

        assertPrimaryApplicant(grantOfRepresentation);

        assertAssets(grantOfRepresentation);

        assertDeceased(grantOfRepresentation);

        assertDeclaration(grantOfRepresentation);

        assertIht(grantOfRepresentation);

        assertPayment(grantOfRepresentation);

        assertRegistry(grantOfRepresentation);
    }

    private void assertDeclaration(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation
                .getDeclaration().getDeclarationCheckbox(), Matchers.equalTo(Boolean.FALSE));
    }

    private void assertRegistry(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getRegistryLocation(), Matchers.equalTo(REG_ADDRESS));
    }

    private void assertAssets(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getAssetsOverseas(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getAssetsOverseasNetValue(),
                Matchers.equalTo(ASSETS_OVERSEAS_NET_VALUE_LONG));
        Assert.assertThat(grantOfRepresentation.getOutsideUkGrantCopies(), Matchers.equalTo(COPIES_OVERSEAS));
        Assert.assertThat(grantOfRepresentation.getExtraCopiesOfGrant(), Matchers.equalTo(COPIES_UK));
    }

    private void assertPayment(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getPayments(), Matchers.hasSize(1));
        Payment payment = grantOfRepresentation.getPayments().get(0).getValue();
        Assert.assertThat(payment.getAmount(), Matchers.equalTo(PAYMENT_AMOUNT_LONG));
        Assert.assertThat(payment.getDate().toString(), Matchers.equalTo("2019-01-23"));
        Assert.assertThat(payment.getMethod(), Matchers.equalTo(PAYMENT_CHANNEL));
        Assert.assertThat(payment.getStatus(), Matchers.equalTo(PaymentStatus.SUCCESS));
        Assert.assertThat(payment.getTransactionId(), Matchers.equalTo(PAYMENT_TRANSACTION_ID));
    }

    private void assertIht(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getIhtReferenceNumber(),  Matchers.equalTo(IHT_IDENTIFIER));
        Assert.assertThat(grantOfRepresentation.getIhtGrossValue(),  Matchers.equalTo(GROSS_VALUE_LONG));
        Assert.assertThat(grantOfRepresentation.getIhtNetValue(),  Matchers.equalTo(NET_VALUE_LONG));
        Assert.assertThat(grantOfRepresentation.getIhtFormCompletedOnline(),  Matchers.equalTo(Boolean.FALSE));
        Assert.assertThat(grantOfRepresentation.getIhtFormId(),  Matchers.equalTo(IhtFormType.IHT205));
    }

    private void assertDeceased(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getDeceasedAddress().getAddressLine1(), Matchers.equalTo(DECEASED_ADDRESS));
        Assert.assertThat(grantOfRepresentation.getDeceasedForenames(), Matchers.equalTo(DECEASED_FIRST_NAME));
        Assert.assertThat(grantOfRepresentation.getDeceasedSurname(), Matchers.equalTo(DECEASED_LAST_NAME));
        Assert.assertThat(grantOfRepresentation.getDeceasedSpouseNotApplyingReason(),
                Matchers.equalTo(SpouseNotApplyingReason.MENTALLY_INCAPABLE));
        Assert.assertThat(grantOfRepresentation.getDeceasedAnyOtherNames(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getDeceasedDateOfBirth(), Matchers.equalTo(DATE_OF_BIRTH));
        Assert.assertThat(grantOfRepresentation.getDeceasedDateOfDeath(), Matchers.equalTo(DATE_OF_DEATH));
        Assert.assertThat(grantOfRepresentation.getDeceasedDomicileInEngWales(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getDeceasedAddressFound(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation
                .getDeceasedAllDeceasedChildrenOverEighteen(), Matchers.equalTo(Boolean.FALSE));
        Assert.assertThat(grantOfRepresentation
                .getDeceasedAnyDeceasedChildrenDieBeforeDeceased(), Matchers.equalTo(Boolean.FALSE));
        Assert.assertThat(grantOfRepresentation.getDeceasedAnyOtherNames(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation
                .getDeceasedAnyDeceasedGrandchildrenUnderEighteen(), Matchers.equalTo(Boolean.FALSE));
        Assert.assertThat(grantOfRepresentation.getDeceasedAnyChildren(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getDeceasedDivorcedInEnglandOrWales(), Matchers.equalTo(Boolean.FALSE));
        Assert.assertThat(grantOfRepresentation.getDeceasedMaritalStatus(), Matchers.equalTo(MaritalStatus.MARRIED));
        Assert.assertThat(grantOfRepresentation.getDeceasedOtherChildren(), Matchers.equalTo(Boolean.FALSE));
    }

    private void assertPrimaryApplicant(GrantOfRepresentation grantOfRepresentation) {
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantForenames(), Matchers.equalTo(FIRST_NAME));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantAddress().getAddressLine1(),
                Matchers.equalTo(ADDRESS));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantAddress().getPostCode(), Matchers.equalTo(POSTCODE));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantEmailAddress(), Matchers.equalTo(EMAIL));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantPhoneNumber(), Matchers.equalTo(PHONE_NUMBER));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantSurname(), Matchers.equalTo(LAST_NAME));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantAddressFound(), Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantAdoptionInEnglandOrWales(),
                Matchers.equalTo(Boolean.TRUE));
        Assert.assertThat(grantOfRepresentation.getPrimaryApplicantRelationshipToDeceased(),
                Matchers.equalTo(Relationship.CHILD));
    }
}