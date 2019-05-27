package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.Lists;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatementExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Will;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaApplicant;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeclarationDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaLegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaLegalStatementExecutorApplying;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PaTestDataCreator {


    private static final String APPLICANT_EMAIL = "jon.snow.got1234@gmail.com";
    private static final BigDecimal NET_VALUE = new BigDecimal("20000.00");
    private static final BigDecimal GROSS_VALUE = new BigDecimal("20000.00");
    private static final String IDENTIFIER = "IHT1234567";
    private static final Long OVERSEAS = 5L;
    private static final Long UK = 1L;
    private static final boolean CODICILS = true;
    private static final Long ID = 1551365512754035L;
    private static final String STATE = "CaseCreated";
    private static final java.util.Date PAYMENT_DATE = Date.from(LocalDate.of(2019, 2, 28).atStartOfDay().toInstant(ZoneOffset.UTC));
    private static final BigDecimal AMOUNT = new BigDecimal("215.50");
    private static final String SITE_ID = "P223";
    private static final PaymentStatus PAYMENT_STATUS = PaymentStatus.SUCCESS;
    private static final String METHOD = "online";
    private static final String REFERENCE = "RC-1551-3655-7880-7354";
    private static final String TRANSACTION_ID = "kp5i7giksdji0ucuuaktsgt9t7";
    private static final BigDecimal COST_UK = BigDecimal.valueOf(0.5);
    private static final int NUMBER_UK = 1;
    private static final BigDecimal COST_OVERSEAS = BigDecimal.valueOf(0);
    private static final int NUMBER_OVERSEAS = 0;
    private static final boolean DECEASED_ALIAS = false;
    private static final Address DECEASED_ADDRESS = Address.builder().addressLine1("Winterfell")
        .postTown("North Westeros").postCode("GOT123").build();
    private static final boolean MARRIED = false;
    private static final LocalDateTime DECEASED_DATE_OF_BIRTH = LocalDate.of(1900, 1, 1).atStartOfDay();
    private static final LocalDateTime DECEASED_DATE_OF_DEATH = LocalDate.of(2019, 1, 1).atStartOfDay();
    private static final String DECEASED_LAST_NAME = "Stark";
    private static final String DECEASED_FIRST_NAME = "Ned";
    private static final String MANCHESTER = "Manchester";
    private static final String APPLICANT_ALIAS = "King of the North";
    private static final String APPLICANT_ALIAS_REASON = "Title Given";
    private static final Address APPLICANT_ADDRESS = Address.builder().addressLine1("The Wall")
        .postTown("North Westeros").postCode("GOT567").build();
    private static final String APPLICANT__LASTNAME = "Snow";
    private static final String APPLICANT_FIRSTNAME = "Jon";
    private static final String APPLICANT_PHONE_NUMBER = "3234324";
    private static final boolean APPLICANT_NAME_AS_ON_THE_WILL = true;
    private static final boolean FIRST_EXECUTOR_IS_APPLYING = true;
    private static final int EXECUTORS_NUMBER = 4;
    private static final boolean SOFT_STOP = false;
    private static final String ACCEPT = "I confirm that I will administer the estate of the person who died "
        + "according to law, and that my application is truthful.";
    private static final String CONFIRM = "I confirm that I will administer the estate of Ned Stark, "
        + "according to law. I will:";
    private static final String REQUESTS = "If the probate registry (court) asks me to do so, I will:";
    private static final String UNDERSTAND = "I understand that:";
    private static final String CONFIRM_ITEM_1 = "collect the whole estate";
    private static final String CONFIRM_ITEM_2 = "keep full details (an inventory) of the estate";
    private static final String CONFIRM_ITEM_3 = "keep a full account of how the estate has been administered";
    private static final String REQUESTS_ITEM_1 = "provide the full details of the estate "
        + "and how it has been administered";
    private static final String REQUESTS_ITEM_2 = "return the grant of probate to the court";
    private static final String UNDERSTAND_ITEM_1 = "my application will be rejected if I do not answer "
        + "any questions about the information I have given";
    private static final String UNDERSTAND_ITEM_2 = "criminal proceedings for fraud may be brought against "
        + "me if I am found to have been deliberately untruthful or dishonest";
    private static final String INTRO = "This statement is based on the information you&rsquo;ve given "
        + "in your application. It will be stored as a public record.";
    private static final String DECEASED = "Ned Stark was born on 1 January 1900 and died on 1 January 2019, "
        + "domiciled in England and Wales.";
    private static final String APPLICANT = "I, Jon Snow of The Wall, North, Westeros, GOT567, "
        + "make the following statement:";
    private static final String NAME = "I am an executor named in the will as Jon Snow, "
        + "and I am applying for probate.";
    private static final String SIGN = "I will send to the probate registry what I believe to be the "
        + "true and original last will and testament of Ned Stark.";
    private static final String DECEASED_ESTATE_LAND = "To the best of my knowledge, information and belief, "
        + "there was no land vested in Ned Stark which was settled previously to "
        + "the death (and not by the will) of Ned Stark and which remained settled "
        + "land notwithstanding such death.";
    private static final String DECEASED_OTHER_NAMES = "";
    private static final String DECEASED_ESTATE_VALUE = "The gross value for the estate amounts to £20000 and the "
        + "net value for the estate amounts to £20000.";
    private static final long CODICILS_NUMBER = 2L;
    private static final boolean ASSETSOVERSEAS = false;
    private static final String IHT_FORM_ID = "IHT205";
    private static final IhtMethod IHT_METHOD = IhtMethod.ONLINE;
    private static final String FIRST_EXECUTOR_FULLNAME = "Jon Snow";
    private static final String SECOND_EXECUTOR_APPLYING = "Sansa Stark";
    private static final boolean SECOND_EXECUTOR_IS_APPLYING = true;
    private static final String FIRST_EXECUTOR_MOBILE = "123323454";
    private static final String SECOND_EXECUTOR_MOBILE = "8543958430985";
    private static final String FIRST_EXECUTOR_EMAIL = "jon.snow@email.com";
    private static final String SECOND_EXECUTOR_EMAIL = "sansa.stark@email.com";
    private static final boolean FIRST_EXECUTOR_HAS_OTHER_NAME = true;
    private static final String FIRST_EXECUTOR_CURRENT_NAME = "King Of North";
    private static final String FIRST_EXECUTOR_OTHER_REASON = "Given for rule";
    private static final String FIRST_EXECUTOR_CURRENTNAME_REASON = "Given by everyone";
    private static final String FIRST_EXECUTOR_ADDRESS = "Winterfell";
    private static final String SECOND_EXECUTOR_ADDRESS = "Winterfell";
    private static final boolean SECOND_EXECUTOR_HAS_OTHERNAME = false;
    private static final String FIRST_EXECUTOR_NOT_APPLYING = "Rob Stark";
    private static final String SECOND_EXECUTOR_NOT_APPLYING = "Catlin Stark";
    private static final ExecutorNotApplyingReason FIRST_EXECUTOR_NOT_APPLYING_KEY = ExecutorNotApplyingReason.DIED_BEFORE;
    private static final ExecutorNotApplyingReason SECOND_EXECUTOR_NOT_APPLYING_KEY = ExecutorNotApplyingReason.DIED_BEFORE;
    public static final BigDecimal UK_COPIES_FEE = BigDecimal.valueOf(100L).setScale(2, RoundingMode.HALF_UP);
    public static final BigDecimal OVERSEAS_COPIES_FEE = BigDecimal.valueOf(200L).setScale(2, RoundingMode.HALF_UP);
    public static final BigDecimal APPLICATION_FEE = BigDecimal.valueOf(300).setScale(2, RoundingMode.HALF_UP);
    public static final BigDecimal FEES_TOTAL = BigDecimal.valueOf(600L).setScale(2, RoundingMode.HALF_UP);
    public static final String OVERSEAS_COPIES_FEE_CODE = "OVERSEAS1";
    public static final String OVERSEAS_COPIES_FEE_VERSION = "22";
    public static final String UK_COPIES_FEE_CODE = "UK1";
    public static final String UK_COPIES_FEE_VERSION = "1";
    public static final String APPLICATION_FEE_CODE = "APP1";
    public static final String APPLICATION_FEE_VERSION = "33";

    public static PaForm createPaForm() {
        return PaForm.builder()
            .type(ProbateType.PA)
            .applicantEmail(APPLICANT_EMAIL)
            .applicationSubmittedDate(LocalDate.now())
            .iht(InheritanceTax.builder()
                .ihtFormId(IHT_FORM_ID)
                .form(IhtFormType.IHT205)
                .method(IHT_METHOD)
                .netValue(NET_VALUE)
                .grossValue(GROSS_VALUE)
                .netIht205(NET_VALUE)
                .grossIht205(GROSS_VALUE)
                .identifier(IDENTIFIER)
                .build())
            .will(Will.builder()
                .codicils(CODICILS)
                .codicilsNumber(CODICILS_NUMBER)
                .build())
            .copies(
                Copies.builder()
                    .overseas(OVERSEAS)
                    .uk(UK)
                    .build()
            )
            .assets(PaAssets.builder()
                .assetsoverseas(true)
                .build())
            .payment(Payment.builder()
                .date(PAYMENT_DATE)
                .amount(AMOUNT)
                .siteId(SITE_ID)
                .status(PAYMENT_STATUS)
                .method(METHOD)
                .reference(REFERENCE)
                .transactionId(TRANSACTION_ID)
                .build()
            )
            .payments(Lists.newArrayList(Payment.builder()
                .date(PAYMENT_DATE)
                .amount(AMOUNT)
                .siteId(SITE_ID)
                .status(PAYMENT_STATUS)
                .method(METHOD)
                .reference(REFERENCE)
                .transactionId(TRANSACTION_ID)
                .build())
            )
            .deceased(PaDeceased.builder()
                .alias(DECEASED_ALIAS)
                .address(DECEASED_ADDRESS)
                .married(MARRIED)
                .dateOfBirth(DECEASED_DATE_OF_BIRTH)
                .dateOfDeath(DECEASED_DATE_OF_DEATH)
                .lastName(DECEASED_LAST_NAME)
                .firstName(DECEASED_FIRST_NAME)
                .build())
            .registry(Registry.builder()
                .name(MANCHESTER)
                .build())
            .applicant(PaApplicant.builder()
                .alias(APPLICANT_ALIAS)
                .aliasReason(APPLICANT_ALIAS_REASON)
                .address(APPLICANT_ADDRESS)
                .lastName(APPLICANT__LASTNAME)
                .firstName(APPLICANT_FIRSTNAME)
                .phoneNumber(APPLICANT_PHONE_NUMBER)
                .nameAsOnTheWill(APPLICANT_NAME_AS_ON_THE_WILL)
                .build())
            .executors(Executors.builder()
                .list(Lists.newArrayList(
                    Executor.builder()
                        .fullName(SECOND_EXECUTOR_NOT_APPLYING)
                        .notApplyingKey(SECOND_EXECUTOR_NOT_APPLYING_KEY.getOptionValue())
                        .build(),
                    Executor.builder()
                        .fullName(FIRST_EXECUTOR_FULLNAME)
                        .isApplying(FIRST_EXECUTOR_IS_APPLYING)
                        .mobile(FIRST_EXECUTOR_MOBILE)
                        .email(FIRST_EXECUTOR_EMAIL)
                        .address(FIRST_EXECUTOR_ADDRESS)
                        .isApplying(true)
                        .hasOtherName(FIRST_EXECUTOR_HAS_OTHER_NAME)
                        .currentName(FIRST_EXECUTOR_CURRENT_NAME)
                        .currentNameReason(FIRST_EXECUTOR_CURRENTNAME_REASON)
                        .otherReason(FIRST_EXECUTOR_OTHER_REASON)
                        .build(),
                    Executor.builder()
                        .fullName(FIRST_EXECUTOR_NOT_APPLYING)
                        .notApplyingKey(FIRST_EXECUTOR_NOT_APPLYING_KEY.getOptionValue())
                        .build(),
                    Executor.builder()
                        .fullName(SECOND_EXECUTOR_APPLYING)
                        .isApplying(SECOND_EXECUTOR_IS_APPLYING)
                        .mobile(SECOND_EXECUTOR_MOBILE)
                        .email(SECOND_EXECUTOR_EMAIL)
                        .address(SECOND_EXECUTOR_ADDRESS)
                        .isApplying(true)
                        .hasOtherName(SECOND_EXECUTOR_HAS_OTHERNAME)
                        .build()
                ))
                .executorsNumber(EXECUTORS_NUMBER)
                .build())
            .declaration(PaDeclaration.builder()
                .softStop(SOFT_STOP)
                .declaration(
                    PaDeclarationDeclaration.builder()
                        .accept(ACCEPT)
                        .confirm(CONFIRM)
                        .requests(REQUESTS)
                        .understand(UNDERSTAND)
                        .confirmItem1(CONFIRM_ITEM_1)
                        .confirmItem2(CONFIRM_ITEM_2)
                        .confirmItem3(CONFIRM_ITEM_3)
                        .requestsItem1(REQUESTS_ITEM_1)
                        .requestsItem2(REQUESTS_ITEM_2)
                        .understandItem1(UNDERSTAND_ITEM_1)
                        .understandItem2(UNDERSTAND_ITEM_2)
                        .build()
                )
                .legalStatement(PaLegalStatement.builder()
                    .intro(INTRO)
                    .deceased(DECEASED)
                    .applicant(APPLICANT)
                    .executorsApplying(Lists.newArrayList(
                        PaLegalStatementExecutorApplying.builder()
                            .name(NAME)
                            .sign(SIGN)
                            .build()
                    ))
                    .deceasedEstateLand(DECEASED_ESTATE_LAND)
                    .deceasedOtherNames(DECEASED_OTHER_NAMES)
                    .deceasedEstateValue(DECEASED_ESTATE_VALUE)
                    .build())
                .build())
            .fees(Fees.builder()
                .ukCopiesFee(UK_COPIES_FEE)
                .ukCopiesFeeVersion(UK_COPIES_FEE_VERSION)
                .ukCopiesFeeCode(UK_COPIES_FEE_CODE)
                .overseasCopiesFee(OVERSEAS_COPIES_FEE)
                .overseasCopiesFeeVersion(OVERSEAS_COPIES_FEE_VERSION)
                .overseasCopiesFeeCode(OVERSEAS_COPIES_FEE_CODE)
                .applicationFee(APPLICATION_FEE)
                .applicationFeeCode(APPLICATION_FEE_CODE)
                .applicationFeeVersion(APPLICATION_FEE_VERSION)
                .total(FEES_TOTAL)
                .build())
            .build();
    }

    public static GrantOfRepresentationData createGrantOfRepresentation() {
        return GrantOfRepresentationData.builder()
            .grantType(GrantType.GRANT_OF_PROBATE)
            .primaryApplicantAddress(uk.gov.hmcts.reform.probate.model.cases.Address.builder()
                .addressLine1(APPLICANT_ADDRESS.getAddressLine1())
                .postTown(APPLICANT_ADDRESS.getPostTown())
                .postCode(APPLICANT_ADDRESS.getPostCode())
                .build())
            .primaryApplicantEmailAddress(APPLICANT_EMAIL)
            .applicationSubmittedDate(LocalDate.now())
            .registryLocation(RegistryLocation.findRegistryLocationByName(MANCHESTER))
            .softStop(SOFT_STOP)
            .willHasCodicils(CODICILS)
            .willNumberOfCodicils(CODICILS_NUMBER)
            .ihtNetValue(NET_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtGrossValue(GROSS_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtReferenceNumber(IDENTIFIER)
            .ihtFormCompletedOnline(true)
            .ihtFormId(IhtFormType.IHT205)
            .primaryApplicantForenames(APPLICANT_FIRSTNAME)
            .primaryApplicantSurname(APPLICANT__LASTNAME)
            .primaryApplicantSurname(APPLICANT__LASTNAME)
            .primaryApplicantSameWillName(APPLICANT_NAME_AS_ON_THE_WILL)
            .primaryApplicantAlias(APPLICANT_ALIAS)
            .primaryApplicantAliasReason(APPLICANT_ALIAS_REASON)
            .primaryApplicantPhoneNumber(APPLICANT_PHONE_NUMBER)
            .deceasedAnyOtherNames(false)
            .deceasedDateOfBirth(DECEASED_DATE_OF_BIRTH.toLocalDate())
            .deceasedDateOfDeath(DECEASED_DATE_OF_DEATH.toLocalDate())
            .deceasedSurname(DECEASED_LAST_NAME)
            .deceasedForenames(DECEASED_FIRST_NAME)
            .deceasedMarriedAfterWillOrCodicilDate(MARRIED)
            .deceasedAddress(uk.gov.hmcts.reform.probate.model.cases.Address.builder()
                .addressLine1(DECEASED_ADDRESS.getAddressLine1())
                .postTown(DECEASED_ADDRESS.getPostTown())
                .postCode(DECEASED_ADDRESS.getPostCode())
                .build())
            .numberOfApplicants(Long.valueOf(EXECUTORS_NUMBER))
            .numberOfExecutors(Long.valueOf(EXECUTORS_NUMBER))
            .applicationType(ApplicationType.PERSONAL)
            .payments(Lists.newArrayList(
                CollectionMember.<CasePayment>builder()
                    .value(CasePayment.builder()
                        .amount(AMOUNT.multiply(BigDecimal.valueOf(100)).longValue())
                        .status(PaymentStatus.SUCCESS)
                        .date(PAYMENT_DATE)
                        .transactionId(TRANSACTION_ID)
                        .reference(REFERENCE)
                        .siteId(SITE_ID)
                        .method(METHOD)
                        .build())
                    .build()))
            .legalStatement(LegalStatement.builder()
                .intro(INTRO)
                .applicant(APPLICANT)
                .deceased(DECEASED)
                .deceasedEstateLand(DECEASED_ESTATE_LAND)
                .deceasedEstateValue(DECEASED_ESTATE_VALUE)
                .deceasedOtherNames(DECEASED_OTHER_NAMES)
                .executorsApplying(Lists.newArrayList(
                    CollectionMember.<LegalStatementExecutorApplying>builder()
                        .value(LegalStatementExecutorApplying.builder()
                            .sign(SIGN)
                            .name(NAME)
                            .build())
                        .build()
                ))
                .executorsNotApplying(null)
                .build())
            .declaration(Declaration.builder()
                .accept(ACCEPT)
                .confirm(CONFIRM)
                .confirmItem1(CONFIRM_ITEM_1)
                .confirmItem2(CONFIRM_ITEM_2)
                .confirmItem3(CONFIRM_ITEM_3)
                .requests(REQUESTS)
                .requestsItem1(REQUESTS_ITEM_1)
                .requestsItem2(REQUESTS_ITEM_2)
                .understand(UNDERSTAND)
                .understandItem1(UNDERSTAND_ITEM_1)
                .understandItem2(UNDERSTAND_ITEM_2)
                .build())
            .extraCopiesOfGrant(UK)
            .outsideUkGrantCopies(OVERSEAS)
            .executorsNotApplying(Lists.newArrayList(
                CollectionMember.<ExecutorNotApplying>builder()
                    .value(ExecutorNotApplying.builder()
                        .notApplyingExecutorName(SECOND_EXECUTOR_NOT_APPLYING)
                        .notApplyingExecutorReason(SECOND_EXECUTOR_NOT_APPLYING_KEY)
                        .build())
                    .build(),
                CollectionMember.<ExecutorNotApplying>builder()
                    .value(ExecutorNotApplying.builder()
                        .notApplyingExecutorName(FIRST_EXECUTOR_NOT_APPLYING)
                        .notApplyingExecutorReason(FIRST_EXECUTOR_NOT_APPLYING_KEY)
                        .build()).build()
            ))
            .executorsApplying(Lists.newArrayList(
                CollectionMember.<ExecutorApplying>builder()
                    .value(ExecutorApplying.builder()
                        .applyingExecutorName(FIRST_EXECUTOR_FULLNAME)
                        .applyingExecutorPhoneNumber(FIRST_EXECUTOR_MOBILE)
                        .applyingExecutorEmail(FIRST_EXECUTOR_EMAIL)
                        .applyingExecutorAddress(uk.gov.hmcts.reform.probate.model.cases.Address.builder()
                            .addressLine1(FIRST_EXECUTOR_ADDRESS)
                            .build())
                        .applyingExecutorOtherNames(FIRST_EXECUTOR_CURRENT_NAME)
                        .applyingExecutorOtherNamesReason(FIRST_EXECUTOR_CURRENTNAME_REASON)
                        .applyingExecutorOtherReason(FIRST_EXECUTOR_OTHER_REASON)
                        .build()).build(),
                CollectionMember.<ExecutorApplying>builder()
                    .value(ExecutorApplying.builder()
                        .applyingExecutorName(SECOND_EXECUTOR_APPLYING)
                        .applyingExecutorPhoneNumber(SECOND_EXECUTOR_MOBILE)
                        .applyingExecutorEmail(SECOND_EXECUTOR_EMAIL)
                        .applyingExecutorAddress(uk.gov.hmcts.reform.probate.model.cases.Address.builder()
                            .addressLine1(SECOND_EXECUTOR_ADDRESS)
                            .build())
                        .build())
                    .build()
            ))
            .fees(ProbateCalculatedFees.builder()
                .ukCopiesFee(UK_COPIES_FEE.multiply(BigDecimal.valueOf(100)).longValue())
                .ukCopiesFeeVersion(UK_COPIES_FEE_VERSION)
                .ukCopiesFeeCode(UK_COPIES_FEE_CODE)
                .overseasCopiesFee(OVERSEAS_COPIES_FEE.multiply(BigDecimal.valueOf(100)).longValue())
                .overseasCopiesFeeVersion(OVERSEAS_COPIES_FEE_VERSION)
                .overseasCopiesFeeCode(OVERSEAS_COPIES_FEE_CODE)
                .applicationFee(APPLICATION_FEE.multiply(BigDecimal.valueOf(100)).longValue())
                .applicationFeeCode(APPLICATION_FEE_CODE)
                .applicationFeeVersion(APPLICATION_FEE_VERSION)
                .total(FEES_TOTAL.multiply(BigDecimal.valueOf(100)).longValue())
                .build()
            )

            .build();
    }
}
