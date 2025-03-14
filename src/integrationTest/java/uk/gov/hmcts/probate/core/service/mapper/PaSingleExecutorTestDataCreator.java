package uk.gov.hmcts.probate.core.service.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.IhtFormEstate;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.DeathCertificate;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.cases.DocumentType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;
import uk.gov.hmcts.reform.probate.model.cases.UploadDocument;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.CombinedName;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Damage;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationHolder;
import uk.gov.hmcts.reform.probate.model.forms.DocumentUpload;
import uk.gov.hmcts.reform.probate.model.forms.Documents;
import uk.gov.hmcts.reform.probate.model.forms.Equality;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatementExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatementHolder;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.ProvideInformation;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.ReviewResponse;
import uk.gov.hmcts.reform.probate.model.forms.Will;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaApplicant;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public final class PaSingleExecutorTestDataCreator {

    public static final String REGISTRY_ADDRESS = "old trafford";
    public static final String REGISTRY_EMAIL_ADDRESS = "manchester@hmcts.com";
    public static final long REGISTRY_SEQUENCE_NUMBER = 1L;
    public static final String WELSH = "WELSH";
    public static final String PCQ_ID = "1000";
    private static final String APPLICANT_EMAIL = "mainapplicant@gmail.com";
    private static final BigDecimal NET_VALUE = new BigDecimal("20000.00");
    private static final BigDecimal GROSS_VALUE = new BigDecimal("20000.00");
    private static final BigDecimal ESTATE_GROSS_VALUE = new BigDecimal("30000.00");
    private static final String ESTATE_GROSS_VALUE_FIELD = "30000";
    private static final BigDecimal ESTATE_NET_VALUE = new BigDecimal("20000.00");
    private static final String ESTATE_NET_VALUE_FIELD = "20000";
    private static final BigDecimal ESTATE_NET_QUALIFYING_VALUE = new BigDecimal("10000.00");
    private static final String ESTATE_NET_QUALIFYING_VALUE_FIELD = "10000";
    private static final String IDENTIFIER = "IHT1234567";
    private static final Long OVERSEAS = 0L;
    private static final Long UK = 1L;
    private static final boolean CODICILS = true;
    private static final Long ID = 1551365512754035L;
    private static final String STATE = "CaseCreated";
    private static final java.util.Date PAYMENT_DATE =
        Date.from(LocalDate.of(2019, 2, 28).atStartOfDay().toInstant(ZoneOffset.UTC));
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
    private static final boolean DECEASED_DIED_ENG_OR_WALES = true;
    private static final String DECEASED_DEATH_CERTIFICATE = "optionDeathCertificate";
    private static final boolean DECEASED_ALIAS = false;
    private static final boolean DECEASED_ENGLISH_FOREIGN_DEATH_CERT = false;
    private static final boolean DECEASED_FOREIGN_DEATH_CERT_TRANSLATION = true;
    private static final Address DECEASED_ADDRESS = Address.builder().addressLine1("Winterfell")
        .postTown("North Westeros").postCode("GOT123").formattedAddress("Winterfell North Westeros GOT123").build();
    private static final boolean MARRIED = false;
    private static final LocalDateTime DECEASED_DATE_OF_BIRTH = LocalDate.of(1900, 1, 1)
        .atStartOfDay();
    private static final LocalDateTime DECEASED_DATE_OF_DEATH = LocalDate.of(2019, 1, 1)
        .atStartOfDay();
    private static final String DECEASED_LAST_NAME = "Stark";
    private static final String DECEASED_FIRST_NAME = "Ned";
    private static final String DECEASED_POSTCODE = "HA6";
    private static final String MANCHESTER = "Manchester";
    private static final String APPLICANT_ALIAS = "Main Applicant Alias";
    private static final Address APPLICANT_ADDRESS = Address.builder().addressLine1("The Wall")
        .postTown("North Westeros").postCode("GOT567").formattedAddress("The Wall North Westeros GOT567").build();
    private static final String APPLICANT__LASTNAME = "Applicant";
    private static final String APPLICANT_FIRSTNAME = "Main";
    private static final String APPLICANT_PHONE_NUMBER = "3234324";
    private static final String APPLICANT_POSTCODE = "HA5";
    private static final boolean APPLICANT_NAME_AS_ON_THE_WILL = true;
    private static final boolean FIRST_EXECUTOR_IS_APPLYING = true;
    private static final int EXECUTORS_NUMBER = 1;
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
    private static final String APPLICANT = "I, Main Applicant of The Wall, North, Westeros, GOT567, "
        + "make the following statement:";
    private static final String NAME = "I am an executor named in the will as Main Applicant, "
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
    private static final String FIRST_EXECUTOR_FULLNAME = "Main Applicant";
    private static final String FIRST_EXECUTOR_MOBILE = "123323454";
    private static final String FIRST_EXECUTOR_EMAIL = "jon.snow@email.com";
    private static final boolean FIRST_EXECUTOR_HAS_OTHER_NAME = true;
    private static final String FIRST_EXECUTOR_CURRENT_NAME = "King Of North";
    private static final String FIRST_EXECUTOR_OTHER_REASON = "Given for rule";
    private static final String FIRST_EXECUTOR_CURRENTNAME_REASON = "Given by everyone";
    private static final String FIRST_EXECUTOR_ADDRESS = "Winterfell";
    private static final String FIRST_EXECUTOR_INVITE_ID = "123345546";
    private static final String FIRST_EXECUTOR_POSTCODE = "HA7";
    private static final BigDecimal UK_COPIES_FEE = BigDecimal.valueOf(100L).setScale(2,
        RoundingMode.HALF_UP);
    private static final BigDecimal OVERSEAS_COPIES_FEE = BigDecimal.valueOf(200L).setScale(2,
        RoundingMode.HALF_UP);
    private static final BigDecimal APPLICATION_FEE = BigDecimal.valueOf(300).setScale(2,
        RoundingMode.HALF_UP);
    private static final BigDecimal FEES_TOTAL = BigDecimal.valueOf(600L).setScale(2,
        RoundingMode.HALF_UP);
    private static final String OVERSEAS_COPIES_FEE_CODE = "OVERSEAS1";
    private static final String OVERSEAS_COPIES_FEE_VERSION = "22";
    private static final String UK_COPIES_FEE_CODE = "UK1";
    private static final String UK_COPIES_FEE_VERSION = "1";
    private static final String APPLICATION_FEE_CODE = "APP1";
    private static final String APPLICATION_FEE_VERSION = "33";
    private static final String DOCUMENT_URL = "http://doc-url";
    private static final String DOCUMENT_FILENAME = "document_filename.png";
    private static final String SOT_DOCUMENT_URL = "http://sot-doc-url";
    private static final String SOT_DOCUMENT_FILENAME = "sot_document_filename.png";
    private static final List<String> SELECTED_DAMAGE_TYPES = Arrays.asList("stapleOrPunchHoles","paperClipMarks", 
        "tornEdges", "otherVisibleDamage");
    private static final String DAMAGE_TYPE_OTHER_DESC = "Some other damage description";
    private static final boolean DAMAGE_REASON_KNOWN = true;
    private static final String DAMAGE_REASON_DESC = "Some other damage description";
    private static final boolean DAMAGE_CULPRIT_KNOWN = true;
    private static final String DAMAGE_CULPRIT_FIRST_NAME = "CilpritFirst";
    private static final String DAMAGE_CULPRIT_LAST_NAME = "CulpritLast";
    private static final boolean DAMAGE_DATE_KNOWN = true;
    private static final String DAMAGE_DATE = "4/10/2021";
    private static final boolean CODICILS_DAMAGE_REASON_KNOWN = true;
    private static final String CODICILS_DAMAGE_REASON_DESC = "Some other codicil damage description";
    private static final boolean CODICILS_DAMAGE_CULPRIT_KNOWN = true;
    private static final String CODICILS_DAMAGE_CULPRIT_FIRST_NAME = "CodicilCilpritFirst";
    private static final String CODICILS_DAMAGE_CULPRIT_LAST_NAME = "CodicilCulpritLast";
    private static final boolean CODICILS_DAMAGE_DATE_KNOWN = true;
    private static final String CODICILS_DAMAGE_DATE = "5/10/2021";
    private static final boolean DECEASED_WRITTEN_WISHES = true;
    private static String LEGAL_DECLARATION_JSON =
        "{\"legalDeclaration\":{\"headers\":[\"header0\",\"header1\",\"header2\"],"
            + "\"sections\":[{\"headingType\":\"large\",\"title\":\"section title\","
            + "\"declarationItems\":[{\"title\":\"declaration title\","
            + "\"values\":[\"value0\",\"value1\",\"value2\"]}]}],\"dateCreated\":\"date and time\","
            + "\"deceased\":\"deceased\"}}";

    private static String CHECK_ANSWERS_JSON = "{\"checkAnswersSummary\":{\"sections\":[{\"title\":\"section title\","
        + "\"type\":\"heading-medium\",\"questionAndAnswers\":"
        + "[{\"question\":\"question 1\",\"answers\":[\"answer 1\"]},"
        + "{\"question\":\"question 2\",\"answers\":"
        + "[\"\"]}]}],\"mainParagraph\":\"main paragraph\",\"pageTitle\":\"page title\"}}";

    private static String DECEASED_ADDRESSES =
        "[{\"formatted_address\":\"Adam & Eve 81 Petty France London SW1H 9EX\"}]";

    private static String APPLICANT_ADDRESSES = "[{\"formatted_address\":\"102 Petty France London SW1H 9EX\"}]";

    private static String CITIZEN_RESPONSE = "response";

    private PaSingleExecutorTestDataCreator() {

    }

    public static PaForm createPaForm() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return PaForm.builder()
            .type(ProbateType.PA)
            .caseType(GrantType.GRANT_OF_PROBATE.getName())
            .applicantEmail(APPLICANT_EMAIL)
            .language(Language.builder().bilingual(TRUE).build())
            .iht(InheritanceTax.builder()
                .ihtFormId(IhtFormType.NOTAPPLICABLE.name())
                .form(IhtFormType.NOTAPPLICABLE.name())
                .method(IHT_METHOD)
                .netValue(NET_VALUE)
                .grossValue(GROSS_VALUE)
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
                .identifier(IDENTIFIER)
                .build())
            .will(Will.builder()
                .codicils(CODICILS)
                .codicilsNumber(CODICILS_NUMBER)
                .willDamage(Damage.builder()
                    .damageTypesList(SELECTED_DAMAGE_TYPES)
                    .otherDamageDescription(DAMAGE_TYPE_OTHER_DESC)
                    .build())
                .willDamageReasonKnown(DAMAGE_REASON_KNOWN)
                .willDamageReasonDescription(DAMAGE_REASON_DESC)
                .willDamageCulpritKnown(DAMAGE_CULPRIT_KNOWN)
                .willDamageCulpritName(CombinedName.builder()
                    .firstName(DAMAGE_CULPRIT_FIRST_NAME)
                    .lastName(DAMAGE_CULPRIT_LAST_NAME)
                    .build())
                .willDamageDateKnown(DAMAGE_DATE_KNOWN)
                .willDamageDate(DAMAGE_DATE)
                .codicilsDamage(Damage.builder()
                    .damageTypesList(SELECTED_DAMAGE_TYPES)
                    .otherDamageDescription(DAMAGE_TYPE_OTHER_DESC)
                    .build())
                .codicilsDamageReasonKnown(CODICILS_DAMAGE_REASON_KNOWN)
                .codicilsDamageReasonDescription(CODICILS_DAMAGE_REASON_DESC) 
                .codicilsDamageCulpritKnown(CODICILS_DAMAGE_CULPRIT_KNOWN)
                .codicilsDamageCulpritName(CombinedName.builder()
                    .firstName(CODICILS_DAMAGE_CULPRIT_FIRST_NAME)
                    .lastName(CODICILS_DAMAGE_CULPRIT_LAST_NAME)
                    .build())
                .codicilsDamageDateKnown(CODICILS_DAMAGE_DATE_KNOWN)
                .codicilsDamageDate(CODICILS_DAMAGE_DATE)
                .deceasedWrittenWishes(DECEASED_WRITTEN_WISHES)
                .build())
            .copies(
                Copies.builder()
                    .overseas(OVERSEAS)
                    .uk(UK)
                    .build()
            )
            .assets(PaAssets.builder()
                .assetsoverseas(false)
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
                .englishForeignDeathCert(DECEASED_ENGLISH_FOREIGN_DEATH_CERT)
                .foreignDeathCertTranslation(DECEASED_FOREIGN_DEATH_CERT_TRANSLATION)
                .address(DECEASED_ADDRESS)
                .married(MARRIED)
                .dateOfBirth(DECEASED_DATE_OF_BIRTH)
                .dateOfDeath(DECEASED_DATE_OF_DEATH)
                .lastName(DECEASED_LAST_NAME)
                .firstName(DECEASED_FIRST_NAME)
                .addresses(objectMapper.readValue(DECEASED_ADDRESSES, new TypeReference<List<Map<String, Object>>>() {
                }))
                .diedEngOrWales(DECEASED_DIED_ENG_OR_WALES)
                .deathCertificate(DECEASED_DEATH_CERTIFICATE)
                .postcode(DECEASED_POSTCODE)
                .build())
            .registry(Registry.builder()
                .name(MANCHESTER)
                .email(REGISTRY_EMAIL_ADDRESS)
                .address(REGISTRY_ADDRESS)
                .sequenceNumber(REGISTRY_SEQUENCE_NUMBER)
                .build())
            .applicant(PaApplicant.builder()
                .alias(APPLICANT_ALIAS)
                .aliasReason(AliasReason.MARRIAGE.getDescription())
                .address(APPLICANT_ADDRESS)
                .lastName(APPLICANT__LASTNAME)
                .firstName(APPLICANT_FIRSTNAME)
                .phoneNumber(APPLICANT_PHONE_NUMBER)
                .postcode(APPLICANT_POSTCODE)
                .nameAsOnTheWill(APPLICANT_NAME_AS_ON_THE_WILL)
                .addresses(objectMapper.readValue(APPLICANT_ADDRESSES, new TypeReference<List<Map<String, Object>>>() {
                }))
                .build())
            .executors(Executors.builder()
                .list(Lists.newArrayList(
                    Executor.builder()
                        .fullName(FIRST_EXECUTOR_FULLNAME)
                        .firstName(APPLICANT_FIRSTNAME)
                        .lastName(APPLICANT__LASTNAME)
                        .isApplying(FIRST_EXECUTOR_IS_APPLYING)
                        .isApplicant(TRUE)
                        .build()
                ))
                .invitesSent(TRUE)
                .executorsNumber(EXECUTORS_NUMBER)
                .build())
            .declaration(Declaration.builder()
                .softStop(SOFT_STOP)
                .declaration(
                    DeclarationDeclaration.builder()
                        .en(DeclarationHolder.builder()
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
                            .build())
                        .cy(DeclarationHolder.builder()
                            .accept(ACCEPT + WELSH)
                            .confirm(CONFIRM + WELSH)
                            .requests(REQUESTS + WELSH)
                            .understand(UNDERSTAND + WELSH)
                            .confirmItem1(CONFIRM_ITEM_1 + WELSH)
                            .confirmItem2(CONFIRM_ITEM_2 + WELSH)
                            .confirmItem3(CONFIRM_ITEM_3 + WELSH)
                            .requestsItem1(REQUESTS_ITEM_1 + WELSH)
                            .requestsItem2(REQUESTS_ITEM_2 + WELSH)
                            .understandItem1(UNDERSTAND_ITEM_1 + WELSH)
                            .understandItem2(UNDERSTAND_ITEM_2 + WELSH)
                            .build())
                        .build()
                )
                .legalStatement(LegalStatement.builder().en(
                    LegalStatementHolder.builder()
                        .intro(INTRO)
                        .deceased(DECEASED)
                        .applicant(APPLICANT)
                        .executorsApplying(Lists.newArrayList(
                            LegalStatementExecutorApplying.builder()
                                .name(NAME)
                                .sign(SIGN)
                                .build()
                        ))
                        .deceasedEstateLand(DECEASED_ESTATE_LAND)
                        .deceasedOtherNames(DECEASED_OTHER_NAMES)
                        .deceasedEstateValue(DECEASED_ESTATE_VALUE)
                        .build()).build())
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
            .documents(
                Documents.builder()
                    .uploads(Lists.newArrayList(
                        DocumentUpload.builder()
                            .filename(DOCUMENT_FILENAME)
                            .url(DOCUMENT_URL)
                            .build()
                    ))
                    .build()
            )
            .statementOfTruthDocument(
                DocumentUpload.builder()
                    .filename(SOT_DOCUMENT_FILENAME)
                    .url(SOT_DOCUMENT_URL)
                    .build()
            )
            .provideinformation(ProvideInformation.builder()
                    .citizenResponse(CITIZEN_RESPONSE)
                    .documentUploadIssue(FALSE).build())
            .reviewresponse(ReviewResponse.builder().citizenResponseCheckbox(TRUE).build())
            .legalDeclaration(objectMapper.readValue(LEGAL_DECLARATION_JSON, new TypeReference<Map<String, Object>>() {
            }))
            .checkAnswersSummary(objectMapper.readValue(CHECK_ANSWERS_JSON, new TypeReference<Map<String, Object>>() {
            }))
            .equality(Equality.builder().pcqId(PCQ_ID).build())
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
            .primaryApplicantPostCode(APPLICANT_POSTCODE)
            .primaryApplicantIsApplying(true)
            .languagePreferenceWelsh(TRUE)
            .registryLocation(RegistryLocation.findRegistryLocationByName(MANCHESTER))
            .registryAddress(REGISTRY_ADDRESS)
            .registryEmailAddress(REGISTRY_EMAIL_ADDRESS)
            .registrySequenceNumber(REGISTRY_SEQUENCE_NUMBER)
            .softStop(SOFT_STOP)
            .willHasCodicils(CODICILS)
            .willNumberOfCodicils(CODICILS_NUMBER)
            .ihtNetValue(NET_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtGrossValue(GROSS_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtReferenceNumber(IDENTIFIER)
            .ihtFormCompletedOnline(true)
            .ihtFormId(IhtFormType.NOTAPPLICABLE)
            .ihtFormEstateValuesCompleted(TRUE)
            .ihtFormEstate(IhtFormEstate.optionIHT400421)
            .ihtEstateGrossValue(ESTATE_GROSS_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtEstateGrossValueField(ESTATE_GROSS_VALUE_FIELD)
            .ihtEstateNetValue(ESTATE_NET_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtEstateNetValueField(ESTATE_NET_VALUE_FIELD)
            .ihtEstateNetQualifyingValue(ESTATE_NET_QUALIFYING_VALUE.multiply(BigDecimal.valueOf(100)).longValue())
            .ihtEstateNetQualifyingValueField(ESTATE_NET_QUALIFYING_VALUE_FIELD)
            .deceasedHadLateSpouseOrCivilPartner(TRUE)
            .ihtUnusedAllowanceClaimed(TRUE)
            .primaryApplicantForenames(APPLICANT_FIRSTNAME)
            .primaryApplicantSurname(APPLICANT__LASTNAME)
            .primaryApplicantSurname(APPLICANT__LASTNAME)
            .primaryApplicantSameWillName(APPLICANT_NAME_AS_ON_THE_WILL)
            .primaryApplicantAlias(APPLICANT_ALIAS)
            .primaryApplicantAliasReason(AliasReason.MARRIAGE)
            .primaryApplicantPhoneNumber(APPLICANT_PHONE_NUMBER)
            .deceasedAnyOtherNames(false)
            .deceasedForeignDeathCertInEnglish(false)
            .deceasedForeignDeathCertTranslation(true)
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
            .deceasedPostCode(DECEASED_POSTCODE)
            .deceasedDiedEngOrWales(DECEASED_DIED_ENG_OR_WALES)
            .deceasedDeathCertificate(DeathCertificate.DEATH_CERTIFICATE)
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
            .legalStatement(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement.builder()
                .intro(INTRO)
                .applicant(APPLICANT)
                .deceased(DECEASED)
                .deceasedEstateLand(DECEASED_ESTATE_LAND)
                .deceasedEstateValue(DECEASED_ESTATE_VALUE)
                .deceasedOtherNames(DECEASED_OTHER_NAMES)
                .executorsApplying(Lists.newArrayList(
                    CollectionMember.<uk.gov.hmcts.reform.probate.model.cases
                        .grantofrepresentation.LegalStatementExecutorApplying>builder()
                        .value(
                            uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatementExecutorApplying
                                .builder()
                                .sign(SIGN)
                                .name(NAME)
                                .build())
                        .build()
                ))
                .executorsNotApplying(null)
                .build())
            .declaration(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.builder()
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
            .welshDeclaration(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.builder()
                .accept(ACCEPT + WELSH)
                .confirm(CONFIRM + WELSH)
                .requests(REQUESTS + WELSH)
                .understand(UNDERSTAND + WELSH)
                .confirmItem1(CONFIRM_ITEM_1 + WELSH)
                .confirmItem2(CONFIRM_ITEM_2 + WELSH)
                .confirmItem3(CONFIRM_ITEM_3 + WELSH)
                .requestsItem1(REQUESTS_ITEM_1 + WELSH)
                .requestsItem2(REQUESTS_ITEM_2 + WELSH)
                .understandItem1(UNDERSTAND_ITEM_1 + WELSH)
                .understandItem2(UNDERSTAND_ITEM_2 + WELSH)
                .build())
            .extraCopiesOfGrant(UK)
            .outsideUkGrantCopies(OVERSEAS)
            .executorsApplying(Lists.newArrayList(
            ))
            .executorsNotApplying(Lists.newArrayList(
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
            .citizenDocumentsUploaded(Lists.newArrayList(
                CollectionMember.<UploadDocument>builder()
                    .value(UploadDocument.builder()
                        .documentLink(DocumentLink.builder()
                            .documentUrl(DOCUMENT_URL)
                            .documentFilename(DOCUMENT_FILENAME)

                            .documentBinaryUrl(DOCUMENT_URL + "/binary")
                            .build())
                        .comment(DOCUMENT_FILENAME)
                         .documentType(DocumentType.CITIZEN_HUB_UPLOAD)
                        .build())
                    .build()

            ))
            .statementOfTruthDocument(DocumentLink.builder()
                .documentBinaryUrl(SOT_DOCUMENT_URL + "/binary")
                .documentUrl(SOT_DOCUMENT_URL)
                .documentFilename(SOT_DOCUMENT_FILENAME)
                .build())
            .legalDeclarationJson(LEGAL_DECLARATION_JSON)
            .checkAnswersSummaryJson(CHECK_ANSWERS_JSON)
            .deceasedAddresses(DECEASED_ADDRESSES)
            .primaryApplicantAddresses(APPLICANT_ADDRESSES)
            .pcqId(PCQ_ID)
            .willDamage(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Damage.builder()
                .damageTypesList(SELECTED_DAMAGE_TYPES)
                .otherDamageDescription(DAMAGE_TYPE_OTHER_DESC)
                .build())
            .willDamageReasonKnown(DAMAGE_REASON_KNOWN)
            .willDamageReasonDescription(DAMAGE_REASON_DESC)
            .willDamageCulpritKnown(DAMAGE_CULPRIT_KNOWN)
            .willDamageCulpritName(uk.gov.hmcts.reform.probate.model.cases.CombinedName.builder()
                .firstName(DAMAGE_CULPRIT_FIRST_NAME)
                .lastName(DAMAGE_CULPRIT_LAST_NAME)
                .build())
            .willDamageDateKnown(DAMAGE_DATE_KNOWN)
            .willDamageDate(DAMAGE_DATE)
            .codicilsDamage(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Damage.builder()
                .damageTypesList(SELECTED_DAMAGE_TYPES)
                .otherDamageDescription(DAMAGE_TYPE_OTHER_DESC)
                .build())
            .codicilsDamageReasonKnown(CODICILS_DAMAGE_REASON_KNOWN)
            .codicilsDamageReasonDescription(CODICILS_DAMAGE_REASON_DESC)
            .codicilsDamageCulpritKnown(CODICILS_DAMAGE_CULPRIT_KNOWN)
            .codicilsDamageCulpritName(uk.gov.hmcts.reform.probate.model.cases.CombinedName.builder()
                .firstName(CODICILS_DAMAGE_CULPRIT_FIRST_NAME)
                .lastName(CODICILS_DAMAGE_CULPRIT_LAST_NAME)
                .build())
            .codicilsDamageDateKnown(CODICILS_DAMAGE_DATE_KNOWN)
            .codicilsDamageDate(CODICILS_DAMAGE_DATE)
            .deceasedWrittenWishes(DECEASED_WRITTEN_WISHES)
            .citizenResponse(CITIZEN_RESPONSE)
            .documentUploadIssue(FALSE)
            .citizenResponseCheckbox(TRUE)
            .build();
    }
}
