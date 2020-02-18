package uk.gov.hmcts.probate.core.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.UploadDocument;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.DeclarationBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData.GrantOfRepresentationDataBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Copies.CopiesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationDeclaration.DeclarationDeclarationBuilder;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationHolder;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationHolder.DeclarationHolderBuilder;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax.InheritanceTaxBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.Language.LanguageBuilder;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement.LegalStatementBuilder;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatementHolder;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Registry.RegistryBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Will;
import uk.gov.hmcts.reform.probate.model.forms.Will.WillBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors.ExecutorsBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaApplicant;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaApplicant.PaApplicantBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets.PaAssetsBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased.PaDeceasedBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm.PaFormBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-18T11:21:55+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class PaMapperImpl implements PaMapper {

    @Autowired
    private PaPaymentMapper paPaymentMapper;
    @Autowired
    private PaymentsMapper paymentsMapper;
    @Autowired
    private AliasNameMapper aliasNameMapper;
    @Autowired
    private RegistryLocationMapper registryLocationMapper;
    @Autowired
    private PoundsConverter poundsConverter;
    @Autowired
    private IhtMethodConverter ihtMethodConverter;
    @Autowired
    private LegalStatementMapper legalStatementMapper;
    @Autowired
    private ExecutorsMapper executorsMapper;
    @Autowired
    private MapConverter mapConverter;
    @Autowired
    private LocalDateTimeMapper localDateTimeMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private FeesMapper feesMapper;
    @Autowired
    private DocumentsMapper documentsMapper;
    @Autowired
    private StatementOfTruthMapper statementOfTruthMapper;

    @Override
    public GrantOfRepresentationData toCaseData(PaForm form) {
        if ( form == null ) {
            return null;
        }

        GrantOfRepresentationDataBuilder grantOfRepresentationData = GrantOfRepresentationData.builder();

        String netValueField = formIhtNetValueField( form );
        if ( netValueField != null ) {
            grantOfRepresentationData.ihtNetValueField( netValueField );
        }
        Address address = formDeceasedAddress( form );
        if ( address != null ) {
            grantOfRepresentationData.deceasedAddress( addressMapper.toCaseAddress( address ) );
        }
        try {
            if ( form.getCheckAnswersSummary() != null ) {
                grantOfRepresentationData.checkAnswersSummaryJson( mapConverter.fromMap( form.getCheckAnswersSummary() ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        Boolean codicils = formWillCodicils( form );
        if ( codicils != null ) {
            grantOfRepresentationData.willHasCodicils( codicils );
        }
        LocalDateTime dateOfBirth = formDeceasedDateOfBirth( form );
        if ( dateOfBirth != null ) {
            grantOfRepresentationData.deceasedDateOfBirth( localDateTimeMapper.convertLocalDateTimeToLocalDate( dateOfBirth ) );
        }
        Boolean addressFound = formDeceasedAddressFound( form );
        if ( addressFound != null ) {
            grantOfRepresentationData.deceasedAddressFound( addressFound );
        }
        if ( form.getStatementOfTruthDocument() != null ) {
            grantOfRepresentationData.statementOfTruthDocument( statementOfTruthMapper.toUploadDocuments( form.getStatementOfTruthDocument() ) );
        }
        List<Executor> list = formExecutorsList( form );
        List<CollectionMember<ExecutorNotApplying>> list1 = executorsMapper.toExecutorNotApplyingCollectionMember( list );
        if ( list1 != null ) {
            grantOfRepresentationData.executorsNotApplying( list1 );
        }
        LocalDateTime dateOfDeath = formDeceasedDateOfDeath( form );
        if ( dateOfDeath != null ) {
            grantOfRepresentationData.deceasedDateOfDeath( localDateTimeMapper.convertLocalDateTimeToLocalDate( dateOfDeath ) );
        }
        String firstName = formApplicantFirstName( form );
        if ( firstName != null ) {
            grantOfRepresentationData.primaryApplicantForenames( firstName );
        }
        String otherReason = formApplicantOtherReason( form );
        if ( otherReason != null ) {
            grantOfRepresentationData.primaryApplicantOtherReason( otherReason );
        }
        try {
            List<Map<String, Object>> addresses = formApplicantAddresses( form );
            if ( addresses != null ) {
                grantOfRepresentationData.primaryApplicantAddresses( mapConverter.fromMapList( addresses ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        String lastName = formDeceasedLastName( form );
        if ( lastName != null ) {
            grantOfRepresentationData.deceasedSurname( lastName );
        }
        Long sequenceNumber = formRegistrySequenceNumber( form );
        if ( sequenceNumber != null ) {
            grantOfRepresentationData.registrySequenceNumber( sequenceNumber );
        }
        List<CollectionMember<UploadDocument>> list2 = documentsMapper.toUploadDocuments( form.getDocuments() );
        if ( list2 != null ) {
            grantOfRepresentationData.boDocumentsUploaded( list2 );
        }
        Long codicilsNumber = formWillCodicilsNumber( form );
        if ( codicilsNumber != null ) {
            grantOfRepresentationData.willNumberOfCodicils( codicilsNumber );
        }
        LegalStatementHolder cy = formDeclarationLegalStatementCy( form );
        if ( cy != null ) {
            grantOfRepresentationData.welshLegalStatement( legalStatementMapper.toCaseLegalStatement( cy ) );
        }
        List<Executor> list3 = formExecutorsList( form );
        List<CollectionMember<ExecutorApplying>> list4 = executorsMapper.toExecutorApplyingCollectionMember( list3 );
        if ( list4 != null ) {
            grantOfRepresentationData.executorsApplying( list4 );
        }
        BigDecimal grossValue = formIhtGrossValue( form );
        if ( grossValue != null ) {
            grantOfRepresentationData.ihtGrossValue( poundsConverter.poundsToPennies( grossValue ) );
        }
        String name = formRegistryName( form );
        if ( name != null ) {
            grantOfRepresentationData.registryLocation( registryLocationMapper.toRegistryLocation( name ) );
        }
        String ihtFormId = formIhtIhtFormId( form );
        if ( ihtFormId != null ) {
            grantOfRepresentationData.ihtFormId( Enum.valueOf( IhtFormType.class, ihtFormId ) );
        }
        Boolean declarationCheckbox = formDeclarationDeclarationCheckbox( form );
        if ( declarationCheckbox != null ) {
            grantOfRepresentationData.declarationCheckbox( declarationCheckbox );
        }
        LegalStatementHolder en = formDeclarationLegalStatementEn( form );
        if ( en != null ) {
            grantOfRepresentationData.legalStatement( legalStatementMapper.toCaseLegalStatement( en ) );
        }
        try {
            List<Map<String, Object>> addresses1 = formDeceasedAddresses( form );
            if ( addresses1 != null ) {
                grantOfRepresentationData.deceasedAddresses( mapConverter.fromMapList( addresses1 ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        DeclarationHolder cy1 = formDeclarationDeclarationCy( form );
        if ( cy1 != null ) {
            grantOfRepresentationData.welshDeclaration( declarationHolderToDeclaration( cy1 ) );
        }
        Boolean otherExecutorsApplying = formExecutorsOtherExecutorsApplying( form );
        if ( otherExecutorsApplying != null ) {
            grantOfRepresentationData.otherExecutorsApplying( otherExecutorsApplying );
        }
        Boolean alias = formDeceasedAlias( form );
        if ( alias != null ) {
            grantOfRepresentationData.deceasedAnyOtherNames( alias );
        }
        Boolean addressFound1 = formApplicantAddressFound( form );
        if ( addressFound1 != null ) {
            grantOfRepresentationData.primaryApplicantAddressFound( addressFound1 );
        }
        List<CollectionMember<CasePayment>> list5 = paPaymentMapper.paymentToCasePaymentCollectionMembers( form.getPayment() );
        if ( list5 != null ) {
            grantOfRepresentationData.payments( list5 );
        }
        if ( form.getApplicantEmail() != null ) {
            grantOfRepresentationData.primaryApplicantEmailAddress( form.getApplicantEmail() );
        }
        try {
            if ( form.getLegalDeclaration() != null ) {
                grantOfRepresentationData.legalDeclarationJson( mapConverter.fromMap( form.getLegalDeclaration() ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        Boolean alias1 = formExecutorsAlias( form );
        if ( alias1 != null ) {
            grantOfRepresentationData.executorsHaveAlias( alias1 );
        }
        String email = formRegistryEmail( form );
        if ( email != null ) {
            grantOfRepresentationData.registryEmailAddress( email );
        }
        Address address1 = formApplicantAddress( form );
        if ( address1 != null ) {
            grantOfRepresentationData.primaryApplicantAddress( addressMapper.toCaseAddress( address1 ) );
        }
        Boolean allalive = formExecutorsAllalive( form );
        if ( allalive != null ) {
            grantOfRepresentationData.executorsAllAlive( allalive );
        }
        String phoneNumber = formApplicantPhoneNumber( form );
        if ( phoneNumber != null ) {
            grantOfRepresentationData.primaryApplicantPhoneNumber( phoneNumber );
        }
        BigDecimal netValue = formIhtNetValue( form );
        if ( netValue != null ) {
            grantOfRepresentationData.ihtNetValue( poundsConverter.poundsToPennies( netValue ) );
        }
        Boolean married = formDeceasedMarried( form );
        if ( married != null ) {
            grantOfRepresentationData.deceasedMarriedAfterWillOrCodicilDate( married );
        }
        Boolean nameAsOnTheWill = formApplicantNameAsOnTheWill( form );
        if ( nameAsOnTheWill != null ) {
            grantOfRepresentationData.primaryApplicantSameWillName( nameAsOnTheWill );
        }
        Boolean softStop = formDeclarationSoftStop( form );
        if ( softStop != null ) {
            grantOfRepresentationData.softStop( softStop );
        }
        Map<String, AliasOtherNames> otherNames = formDeceasedOtherNames( form );
        List<CollectionMember<AliasName>> list6 = aliasNameMapper.toCollectionMember( otherNames );
        if ( list6 != null ) {
            grantOfRepresentationData.deceasedAliasNameList( list6 );
        }
        Long uk = formCopiesUk( form );
        if ( uk != null ) {
            grantOfRepresentationData.extraCopiesOfGrant( uk );
        }
        String grossValueField = formIhtGrossValueField( form );
        if ( grossValueField != null ) {
            grantOfRepresentationData.ihtGrossValueField( grossValueField );
        }
        Integer executorsNumber = formExecutorsExecutorsNumber( form );
        if ( executorsNumber != null ) {
            grantOfRepresentationData.numberOfExecutors( executorsNumber.longValue() );
        }
        String firstName1 = formDeceasedFirstName( form );
        if ( firstName1 != null ) {
            grantOfRepresentationData.deceasedForenames( firstName1 );
        }
        String postcode = formApplicantPostcode( form );
        if ( postcode != null ) {
            grantOfRepresentationData.primaryApplicantPostCode( postcode );
        }
        DeclarationHolder en1 = formDeclarationDeclarationEn( form );
        if ( en1 != null ) {
            grantOfRepresentationData.declaration( declarationHolderToDeclaration( en1 ) );
        }
        String address2 = formRegistryAddress( form );
        if ( address2 != null ) {
            grantOfRepresentationData.registryAddress( address2 );
        }
        Boolean bilingual = formLanguageBilingual( form );
        if ( bilingual != null ) {
            grantOfRepresentationData.languagePreferenceWelsh( bilingual );
        }
        String postcode1 = formDeceasedPostcode( form );
        if ( postcode1 != null ) {
            grantOfRepresentationData.deceasedPostCode( postcode1 );
        }
        IhtMethod method = formIhtMethod( form );
        if ( method != null ) {
            grantOfRepresentationData.ihtFormCompletedOnline( ihtMethodConverter.fromIhtMethod( method ) );
        }
        String lastName1 = formApplicantLastName( form );
        if ( lastName1 != null ) {
            grantOfRepresentationData.primaryApplicantSurname( lastName1 );
        }
        String alias2 = formApplicantAlias( form );
        if ( alias2 != null ) {
            grantOfRepresentationData.primaryApplicantAlias( alias2 );
        }
        if ( form.getFees() != null ) {
            grantOfRepresentationData.fees( feesMapper.toProbateCalculatedFees( form.getFees() ) );
        }

        grantOfRepresentationData.applicationType( ApplicationType.PERSONAL );
        grantOfRepresentationData.ihtReferenceNumber( form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? form.getIht().getIdentifier() : "Not applicable" );
        grantOfRepresentationData.grantType( GrantType.GRANT_OF_PROBATE );
        grantOfRepresentationData.applicationSubmittedDate( LocalDate.now() );
        grantOfRepresentationData.numberOfApplicants( form.getExecutors() == null || form.getExecutors().getList() == null ? 0L : Long.valueOf(form.getExecutors().getList().size()) );
        grantOfRepresentationData.primaryApplicantAliasReason( form.getApplicant()!= null && form.getApplicant().getAliasReason() != null ? AliasReason.fromString(form.getApplicant().getAliasReason()) : null );
        grantOfRepresentationData.outsideUkGrantCopies( OverseasCopiesMapper.mapOverseasCopies(form) );

        return grantOfRepresentationData.build();
    }

    @Override
    public PaForm fromCaseData(GrantOfRepresentationData grantOfRepresentation) {
        if ( grantOfRepresentation == null ) {
            return null;
        }

        PaFormBuilder paForm = PaForm.builder();

        paForm.registry( grantOfRepresentationDataToRegistry( grantOfRepresentation ) );
        paForm.deceased( grantOfRepresentationDataToPaDeceased( grantOfRepresentation ) );
        paForm.language( grantOfRepresentationDataToLanguage( grantOfRepresentation ) );
        paForm.applicant( grantOfRepresentationDataToPaApplicant( grantOfRepresentation ) );
        paForm.declaration( grantOfRepresentationDataToDeclaration( grantOfRepresentation ) );
        paForm.iht( grantOfRepresentationDataToInheritanceTax( grantOfRepresentation ) );
        paForm.executors( grantOfRepresentationDataToExecutors( grantOfRepresentation ) );
        paForm.copies( grantOfRepresentationDataToCopies( grantOfRepresentation ) );
        paForm.will( grantOfRepresentationDataToWill( grantOfRepresentation ) );
        try {
            Map<String, Object> map = mapConverter.toMap( grantOfRepresentation.getCheckAnswersSummaryJson() );
            if ( map != null ) {
                paForm.checkAnswersSummary( map );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentation.getBoDocumentsUploaded() != null ) {
            paForm.documents( documentsMapper.fromUploadDocs( grantOfRepresentation.getBoDocumentsUploaded() ) );
        }
        if ( grantOfRepresentation.getStatementOfTruthDocument() != null ) {
            paForm.statementOfTruthDocument( statementOfTruthMapper.fromDocumentLink( grantOfRepresentation.getStatementOfTruthDocument() ) );
        }
        if ( grantOfRepresentation.getPayments() != null ) {
            paForm.payment( paPaymentMapper.paymentFromCasePaymentCollectionMembers( grantOfRepresentation.getPayments() ) );
        }
        List<Payment> list = paymentsMapper.fromCasePaymentCollectionMembers( grantOfRepresentation.getPayments() );
        if ( list != null ) {
            paForm.payments( list );
        }
        if ( grantOfRepresentation.getPrimaryApplicantEmailAddress() != null ) {
            paForm.applicantEmail( grantOfRepresentation.getPrimaryApplicantEmailAddress() );
        }
        try {
            Map<String, Object> map1 = mapConverter.toMap( grantOfRepresentation.getLegalDeclarationJson() );
            if ( map1 != null ) {
                paForm.legalDeclaration( map1 );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentation.getApplicationSubmittedDate() != null ) {
            paForm.applicationSubmittedDate( grantOfRepresentation.getApplicationSubmittedDate() );
        }
        if ( grantOfRepresentation.getFees() != null ) {
            paForm.fees( feesMapper.fromProbateCalculatedFees( grantOfRepresentation.getFees() ) );
        }
        paForm.assets( grantOfRepresentationDataToPaAssets( grantOfRepresentation ) );

        paForm.type( ProbateType.PA );
        paForm.caseType( GrantType.GRANT_OF_PROBATE.getName() );

        return paForm.build();
    }

    private String formIhtNetValueField(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String netValueField = iht.getNetValueField();
        if ( netValueField == null ) {
            return null;
        }
        return netValueField;
    }

    private Address formDeceasedAddress(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Address address = deceased.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formWillCodicils(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Will will = paForm.getWill();
        if ( will == null ) {
            return null;
        }
        Boolean codicils = will.getCodicils();
        if ( codicils == null ) {
            return null;
        }
        return codicils;
    }

    private LocalDateTime formDeceasedDateOfBirth(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDateTime dateOfBirth = deceased.getDateOfBirth();
        if ( dateOfBirth == null ) {
            return null;
        }
        return dateOfBirth;
    }

    private Boolean formDeceasedAddressFound(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean addressFound = deceased.getAddressFound();
        if ( addressFound == null ) {
            return null;
        }
        return addressFound;
    }

    private List<Executor> formExecutorsList(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Executors executors = paForm.getExecutors();
        if ( executors == null ) {
            return null;
        }
        List<Executor> list = executors.getList();
        if ( list == null ) {
            return null;
        }
        return list;
    }

    private LocalDateTime formDeceasedDateOfDeath(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDateTime dateOfDeath = deceased.getDateOfDeath();
        if ( dateOfDeath == null ) {
            return null;
        }
        return dateOfDeath;
    }

    private String formApplicantFirstName(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String firstName = applicant.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String formApplicantOtherReason(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String otherReason = applicant.getOtherReason();
        if ( otherReason == null ) {
            return null;
        }
        return otherReason;
    }

    private List<Map<String, Object>> formApplicantAddresses(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        List<Map<String, Object>> addresses = applicant.getAddresses();
        if ( addresses == null ) {
            return null;
        }
        return addresses;
    }

    private String formDeceasedLastName(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String lastName = deceased.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private Long formRegistrySequenceNumber(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Registry registry = paForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        Long sequenceNumber = registry.getSequenceNumber();
        if ( sequenceNumber == null ) {
            return null;
        }
        return sequenceNumber;
    }

    private Long formWillCodicilsNumber(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Will will = paForm.getWill();
        if ( will == null ) {
            return null;
        }
        Long codicilsNumber = will.getCodicilsNumber();
        if ( codicilsNumber == null ) {
            return null;
        }
        return codicilsNumber;
    }

    private LegalStatementHolder formDeclarationLegalStatementCy(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        LegalStatement legalStatement = declaration.getLegalStatement();
        if ( legalStatement == null ) {
            return null;
        }
        LegalStatementHolder cy = legalStatement.getCy();
        if ( cy == null ) {
            return null;
        }
        return cy;
    }

    private BigDecimal formIhtGrossValue(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        BigDecimal grossValue = iht.getGrossValue();
        if ( grossValue == null ) {
            return null;
        }
        return grossValue;
    }

    private String formRegistryName(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Registry registry = paForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String name = registry.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String formIhtIhtFormId(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String ihtFormId = iht.getIhtFormId();
        if ( ihtFormId == null ) {
            return null;
        }
        return ihtFormId;
    }

    private Boolean formDeclarationDeclarationCheckbox(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        Boolean declarationCheckbox = declaration.getDeclarationCheckbox();
        if ( declarationCheckbox == null ) {
            return null;
        }
        return declarationCheckbox;
    }

    private LegalStatementHolder formDeclarationLegalStatementEn(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        LegalStatement legalStatement = declaration.getLegalStatement();
        if ( legalStatement == null ) {
            return null;
        }
        LegalStatementHolder en = legalStatement.getEn();
        if ( en == null ) {
            return null;
        }
        return en;
    }

    private List<Map<String, Object>> formDeceasedAddresses(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        List<Map<String, Object>> addresses = deceased.getAddresses();
        if ( addresses == null ) {
            return null;
        }
        return addresses;
    }

    private DeclarationHolder formDeclarationDeclarationCy(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        DeclarationDeclaration declaration1 = declaration.getDeclaration();
        if ( declaration1 == null ) {
            return null;
        }
        DeclarationHolder cy = declaration1.getCy();
        if ( cy == null ) {
            return null;
        }
        return cy;
    }

    protected uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration declarationHolderToDeclaration(DeclarationHolder declarationHolder) {
        if ( declarationHolder == null ) {
            return null;
        }

        DeclarationBuilder declaration = uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.builder();

        if ( declarationHolder.getAccept() != null ) {
            declaration.accept( declarationHolder.getAccept() );
        }
        if ( declarationHolder.getConfirm() != null ) {
            declaration.confirm( declarationHolder.getConfirm() );
        }
        if ( declarationHolder.getRequests() != null ) {
            declaration.requests( declarationHolder.getRequests() );
        }
        if ( declarationHolder.getUnderstand() != null ) {
            declaration.understand( declarationHolder.getUnderstand() );
        }
        if ( declarationHolder.getConfirmItem1() != null ) {
            declaration.confirmItem1( declarationHolder.getConfirmItem1() );
        }
        if ( declarationHolder.getConfirmItem2() != null ) {
            declaration.confirmItem2( declarationHolder.getConfirmItem2() );
        }
        if ( declarationHolder.getConfirmItem3() != null ) {
            declaration.confirmItem3( declarationHolder.getConfirmItem3() );
        }
        if ( declarationHolder.getRequestsItem1() != null ) {
            declaration.requestsItem1( declarationHolder.getRequestsItem1() );
        }
        if ( declarationHolder.getRequestsItem2() != null ) {
            declaration.requestsItem2( declarationHolder.getRequestsItem2() );
        }
        if ( declarationHolder.getUnderstandItem1() != null ) {
            declaration.understandItem1( declarationHolder.getUnderstandItem1() );
        }
        if ( declarationHolder.getUnderstandItem2() != null ) {
            declaration.understandItem2( declarationHolder.getUnderstandItem2() );
        }
        if ( declarationHolder.getSubmitWarning() != null ) {
            declaration.submitWarning( declarationHolder.getSubmitWarning() );
        }

        return declaration.build();
    }

    private Boolean formExecutorsOtherExecutorsApplying(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Executors executors = paForm.getExecutors();
        if ( executors == null ) {
            return null;
        }
        Boolean otherExecutorsApplying = executors.getOtherExecutorsApplying();
        if ( otherExecutorsApplying == null ) {
            return null;
        }
        return otherExecutorsApplying;
    }

    private Boolean formDeceasedAlias(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean alias = deceased.getAlias();
        if ( alias == null ) {
            return null;
        }
        return alias;
    }

    private Boolean formApplicantAddressFound(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Boolean addressFound = applicant.getAddressFound();
        if ( addressFound == null ) {
            return null;
        }
        return addressFound;
    }

    private Boolean formExecutorsAlias(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Executors executors = paForm.getExecutors();
        if ( executors == null ) {
            return null;
        }
        Boolean alias = executors.getAlias();
        if ( alias == null ) {
            return null;
        }
        return alias;
    }

    private String formRegistryEmail(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Registry registry = paForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String email = registry.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private Address formApplicantAddress(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Address address = applicant.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formExecutorsAllalive(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Executors executors = paForm.getExecutors();
        if ( executors == null ) {
            return null;
        }
        Boolean allalive = executors.getAllalive();
        if ( allalive == null ) {
            return null;
        }
        return allalive;
    }

    private String formApplicantPhoneNumber(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String phoneNumber = applicant.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber;
    }

    private BigDecimal formIhtNetValue(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        BigDecimal netValue = iht.getNetValue();
        if ( netValue == null ) {
            return null;
        }
        return netValue;
    }

    private Boolean formDeceasedMarried(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean married = deceased.getMarried();
        if ( married == null ) {
            return null;
        }
        return married;
    }

    private Boolean formApplicantNameAsOnTheWill(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Boolean nameAsOnTheWill = applicant.getNameAsOnTheWill();
        if ( nameAsOnTheWill == null ) {
            return null;
        }
        return nameAsOnTheWill;
    }

    private Boolean formDeclarationSoftStop(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        Boolean softStop = declaration.getSoftStop();
        if ( softStop == null ) {
            return null;
        }
        return softStop;
    }

    private Map<String, AliasOtherNames> formDeceasedOtherNames(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Map<String, AliasOtherNames> otherNames = deceased.getOtherNames();
        if ( otherNames == null ) {
            return null;
        }
        return otherNames;
    }

    private Long formCopiesUk(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Copies copies = paForm.getCopies();
        if ( copies == null ) {
            return null;
        }
        Long uk = copies.getUk();
        if ( uk == null ) {
            return null;
        }
        return uk;
    }

    private String formIhtGrossValueField(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String grossValueField = iht.getGrossValueField();
        if ( grossValueField == null ) {
            return null;
        }
        return grossValueField;
    }

    private Integer formExecutorsExecutorsNumber(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Executors executors = paForm.getExecutors();
        if ( executors == null ) {
            return null;
        }
        Integer executorsNumber = executors.getExecutorsNumber();
        if ( executorsNumber == null ) {
            return null;
        }
        return executorsNumber;
    }

    private String formDeceasedFirstName(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String firstName = deceased.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String formApplicantPostcode(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String postcode = applicant.getPostcode();
        if ( postcode == null ) {
            return null;
        }
        return postcode;
    }

    private DeclarationHolder formDeclarationDeclarationEn(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Declaration declaration = paForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        DeclarationDeclaration declaration1 = declaration.getDeclaration();
        if ( declaration1 == null ) {
            return null;
        }
        DeclarationHolder en = declaration1.getEn();
        if ( en == null ) {
            return null;
        }
        return en;
    }

    private String formRegistryAddress(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Registry registry = paForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String address = registry.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formLanguageBilingual(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        Language language = paForm.getLanguage();
        if ( language == null ) {
            return null;
        }
        Boolean bilingual = language.getBilingual();
        if ( bilingual == null ) {
            return null;
        }
        return bilingual;
    }

    private String formDeceasedPostcode(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaDeceased deceased = paForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String postcode = deceased.getPostcode();
        if ( postcode == null ) {
            return null;
        }
        return postcode;
    }

    private IhtMethod formIhtMethod(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        InheritanceTax iht = paForm.getIht();
        if ( iht == null ) {
            return null;
        }
        IhtMethod method = iht.getMethod();
        if ( method == null ) {
            return null;
        }
        return method;
    }

    private String formApplicantLastName(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String lastName = applicant.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String formApplicantAlias(PaForm paForm) {
        if ( paForm == null ) {
            return null;
        }
        PaApplicant applicant = paForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String alias = applicant.getAlias();
        if ( alias == null ) {
            return null;
        }
        return alias;
    }

    protected Registry grantOfRepresentationDataToRegistry(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        RegistryBuilder registry = Registry.builder();

        if ( grantOfRepresentationData.getRegistrySequenceNumber() != null ) {
            registry.sequenceNumber( grantOfRepresentationData.getRegistrySequenceNumber() );
        }
        if ( grantOfRepresentationData.getRegistryLocation() != null ) {
            registry.name( registryLocationMapper.fromRegistryLocation( grantOfRepresentationData.getRegistryLocation() ) );
        }
        if ( grantOfRepresentationData.getRegistryEmailAddress() != null ) {
            registry.email( grantOfRepresentationData.getRegistryEmailAddress() );
        }
        if ( grantOfRepresentationData.getRegistryAddress() != null ) {
            registry.address( grantOfRepresentationData.getRegistryAddress() );
        }

        return registry.build();
    }

    protected PaDeceased grantOfRepresentationDataToPaDeceased(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        PaDeceasedBuilder paDeceased = PaDeceased.builder();

        if ( grantOfRepresentationData.getDeceasedForenames() != null ) {
            paDeceased.firstName( grantOfRepresentationData.getDeceasedForenames() );
        }
        if ( grantOfRepresentationData.getDeceasedDateOfDeath() != null ) {
            paDeceased.dateOfDeath( localDateTimeMapper.convertLocalDateToLocalDateTime( grantOfRepresentationData.getDeceasedDateOfDeath() ) );
        }
        if ( grantOfRepresentationData.getDeceasedSurname() != null ) {
            paDeceased.lastName( grantOfRepresentationData.getDeceasedSurname() );
        }
        if ( grantOfRepresentationData.getDeceasedAddressFound() != null ) {
            paDeceased.addressFound( grantOfRepresentationData.getDeceasedAddressFound() );
        }
        if ( grantOfRepresentationData.getDeceasedAddress() != null ) {
            paDeceased.address( addressMapper.toFormAddress( grantOfRepresentationData.getDeceasedAddress() ) );
        }
        Map<String, AliasOtherNames> map = aliasNameMapper.fromCollectionMember( grantOfRepresentationData.getDeceasedAliasNameList() );
        if ( map != null ) {
            paDeceased.otherNames( map );
        }
        try {
            List<Map<String, Object>> list = mapConverter.toMapList( grantOfRepresentationData.getDeceasedAddresses() );
            if ( list != null ) {
                paDeceased.addresses( list );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentationData.getDeceasedMarriedAfterWillOrCodicilDate() != null ) {
            paDeceased.married( grantOfRepresentationData.getDeceasedMarriedAfterWillOrCodicilDate() );
        }
        if ( grantOfRepresentationData.getDeceasedPostCode() != null ) {
            paDeceased.postcode( grantOfRepresentationData.getDeceasedPostCode() );
        }
        if ( grantOfRepresentationData.getDeceasedDateOfBirth() != null ) {
            paDeceased.dateOfBirth( localDateTimeMapper.convertLocalDateToLocalDateTime( grantOfRepresentationData.getDeceasedDateOfBirth() ) );
        }
        if ( grantOfRepresentationData.getDeceasedAnyOtherNames() != null ) {
            paDeceased.alias( grantOfRepresentationData.getDeceasedAnyOtherNames() );
        }

        return paDeceased.build();
    }

    protected Language grantOfRepresentationDataToLanguage(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        LanguageBuilder language = Language.builder();

        if ( grantOfRepresentationData.getLanguagePreferenceWelsh() != null ) {
            language.bilingual( grantOfRepresentationData.getLanguagePreferenceWelsh() );
        }

        return language.build();
    }

    protected PaApplicant grantOfRepresentationDataToPaApplicant(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        PaApplicantBuilder paApplicant = PaApplicant.builder();

        if ( grantOfRepresentationData.getPrimaryApplicantPostCode() != null ) {
            paApplicant.postcode( grantOfRepresentationData.getPrimaryApplicantPostCode() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantAddressFound() != null ) {
            paApplicant.addressFound( grantOfRepresentationData.getPrimaryApplicantAddressFound() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantAlias() != null ) {
            paApplicant.alias( grantOfRepresentationData.getPrimaryApplicantAlias() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantSameWillName() != null ) {
            paApplicant.nameAsOnTheWill( grantOfRepresentationData.getPrimaryApplicantSameWillName() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantForenames() != null ) {
            paApplicant.firstName( grantOfRepresentationData.getPrimaryApplicantForenames() );
        }
        try {
            List<Map<String, Object>> list = mapConverter.toMapList( grantOfRepresentationData.getPrimaryApplicantAddresses() );
            if ( list != null ) {
                paApplicant.addresses( list );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantOtherReason() != null ) {
            paApplicant.otherReason( grantOfRepresentationData.getPrimaryApplicantOtherReason() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantPhoneNumber() != null ) {
            paApplicant.phoneNumber( grantOfRepresentationData.getPrimaryApplicantPhoneNumber() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantSurname() != null ) {
            paApplicant.lastName( grantOfRepresentationData.getPrimaryApplicantSurname() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantAddress() != null ) {
            paApplicant.address( addressMapper.toFormAddress( grantOfRepresentationData.getPrimaryApplicantAddress() ) );
        }

        paApplicant.aliasReason( grantOfRepresentationData.getPrimaryApplicantAliasReason()!=null ? grantOfRepresentationData.getPrimaryApplicantAliasReason().getDescription() : null );

        return paApplicant.build();
    }

    protected DeclarationHolder declarationToDeclarationHolder(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration declaration) {
        if ( declaration == null ) {
            return null;
        }

        DeclarationHolderBuilder declarationHolder = DeclarationHolder.builder();

        if ( declaration.getConfirm() != null ) {
            declarationHolder.confirm( declaration.getConfirm() );
        }
        if ( declaration.getConfirmItem1() != null ) {
            declarationHolder.confirmItem1( declaration.getConfirmItem1() );
        }
        if ( declaration.getConfirmItem2() != null ) {
            declarationHolder.confirmItem2( declaration.getConfirmItem2() );
        }
        if ( declaration.getConfirmItem3() != null ) {
            declarationHolder.confirmItem3( declaration.getConfirmItem3() );
        }
        if ( declaration.getRequests() != null ) {
            declarationHolder.requests( declaration.getRequests() );
        }
        if ( declaration.getRequestsItem1() != null ) {
            declarationHolder.requestsItem1( declaration.getRequestsItem1() );
        }
        if ( declaration.getRequestsItem2() != null ) {
            declarationHolder.requestsItem2( declaration.getRequestsItem2() );
        }
        if ( declaration.getUnderstand() != null ) {
            declarationHolder.understand( declaration.getUnderstand() );
        }
        if ( declaration.getUnderstandItem1() != null ) {
            declarationHolder.understandItem1( declaration.getUnderstandItem1() );
        }
        if ( declaration.getUnderstandItem2() != null ) {
            declarationHolder.understandItem2( declaration.getUnderstandItem2() );
        }
        if ( declaration.getAccept() != null ) {
            declarationHolder.accept( declaration.getAccept() );
        }
        if ( declaration.getSubmitWarning() != null ) {
            declarationHolder.submitWarning( declaration.getSubmitWarning() );
        }

        return declarationHolder.build();
    }

    protected DeclarationDeclaration grantOfRepresentationDataToDeclarationDeclaration(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        DeclarationDeclarationBuilder declarationDeclaration = DeclarationDeclaration.builder();

        if ( grantOfRepresentationData.getWelshDeclaration() != null ) {
            declarationDeclaration.cy( declarationToDeclarationHolder( grantOfRepresentationData.getWelshDeclaration() ) );
        }
        if ( grantOfRepresentationData.getDeclaration() != null ) {
            declarationDeclaration.en( declarationToDeclarationHolder( grantOfRepresentationData.getDeclaration() ) );
        }

        return declarationDeclaration.build();
    }

    protected LegalStatement grantOfRepresentationDataToLegalStatement(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        LegalStatementBuilder legalStatement = LegalStatement.builder();

        if ( grantOfRepresentationData.getLegalStatement() != null ) {
            legalStatement.en( legalStatementMapper.fromCaseLegalStatement( grantOfRepresentationData.getLegalStatement() ) );
        }
        if ( grantOfRepresentationData.getWelshLegalStatement() != null ) {
            legalStatement.cy( legalStatementMapper.fromCaseLegalStatement( grantOfRepresentationData.getWelshLegalStatement() ) );
        }

        return legalStatement.build();
    }

    protected Declaration grantOfRepresentationDataToDeclaration(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        uk.gov.hmcts.reform.probate.model.forms.Declaration.DeclarationBuilder declaration = Declaration.builder();

        declaration.declaration( grantOfRepresentationDataToDeclarationDeclaration( grantOfRepresentationData ) );
        declaration.legalStatement( grantOfRepresentationDataToLegalStatement( grantOfRepresentationData ) );
        if ( grantOfRepresentationData.getSoftStop() != null ) {
            declaration.softStop( grantOfRepresentationData.getSoftStop() );
        }
        if ( grantOfRepresentationData.getDeclarationCheckbox() != null ) {
            declaration.declarationCheckbox( grantOfRepresentationData.getDeclarationCheckbox() );
        }

        return declaration.build();
    }

    protected InheritanceTax grantOfRepresentationDataToInheritanceTax(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        InheritanceTaxBuilder inheritanceTax = InheritanceTax.builder();

        if ( grantOfRepresentationData.getIhtFormCompletedOnline() != null ) {
            inheritanceTax.method( ihtMethodConverter.toIhtMethod( grantOfRepresentationData.getIhtFormCompletedOnline() ) );
        }
        if ( grantOfRepresentationData.getIhtNetValue() != null ) {
            inheritanceTax.netValue( poundsConverter.penniesToPounds( grantOfRepresentationData.getIhtNetValue() ) );
        }
        if ( grantOfRepresentationData.getIhtFormId() != null ) {
            inheritanceTax.ihtFormId( grantOfRepresentationData.getIhtFormId().name() );
        }
        if ( grantOfRepresentationData.getIhtGrossValueField() != null ) {
            inheritanceTax.grossValueField( grantOfRepresentationData.getIhtGrossValueField() );
        }
        if ( grantOfRepresentationData.getIhtNetValueField() != null ) {
            inheritanceTax.netValueField( grantOfRepresentationData.getIhtNetValueField() );
        }
        if ( grantOfRepresentationData.getIhtGrossValue() != null ) {
            inheritanceTax.grossValue( poundsConverter.penniesToPounds( grantOfRepresentationData.getIhtGrossValue() ) );
        }

        inheritanceTax.netIht400421( IhtValuesMapper.getNetIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()) );
        inheritanceTax.netIht207( IhtValuesMapper.getNetIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()) );
        inheritanceTax.netIht205( IhtValuesMapper.getNetIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()) );
        inheritanceTax.identifier( grantOfRepresentationData.getIhtReferenceNumber() == null || grantOfRepresentationData.getIhtReferenceNumber().equals("Not applicable") ? null : grantOfRepresentationData.getIhtReferenceNumber() );
        inheritanceTax.form( grantOfRepresentationData.getIhtFormId()!=null ? grantOfRepresentationData.getIhtFormId().name() : null );
        inheritanceTax.grossIht205( IhtValuesMapper.getGrossIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()) );
        inheritanceTax.grossIht400421( IhtValuesMapper.getGrossIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()) );
        inheritanceTax.grossIht207( IhtValuesMapper.getGrossIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()) );

        return inheritanceTax.build();
    }

    protected Executors grantOfRepresentationDataToExecutors(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        ExecutorsBuilder executors = Executors.builder();

        if ( grantOfRepresentationData.getExecutorsAllAlive() != null ) {
            executors.allalive( grantOfRepresentationData.getExecutorsAllAlive() );
        }
        if ( grantOfRepresentationData.getExecutorsHaveAlias() != null ) {
            executors.alias( grantOfRepresentationData.getExecutorsHaveAlias() );
        }
        List<Executor> list = executorsMapper.fromCollectionMember( grantOfRepresentationData );
        if ( list != null ) {
            executors.list( list );
        }
        if ( grantOfRepresentationData.getNumberOfExecutors() != null ) {
            executors.executorsNumber( grantOfRepresentationData.getNumberOfExecutors().intValue() );
        }
        if ( grantOfRepresentationData.getOtherExecutorsApplying() != null ) {
            executors.otherExecutorsApplying( grantOfRepresentationData.getOtherExecutorsApplying() );
        }

        executors.invitesSent( grantOfRepresentationData.haveInvitesBeenSent() );

        return executors.build();
    }

    protected Copies grantOfRepresentationDataToCopies(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        CopiesBuilder copies = Copies.builder();

        if ( grantOfRepresentationData.getExtraCopiesOfGrant() != null ) {
            copies.uk( grantOfRepresentationData.getExtraCopiesOfGrant() );
        }
        if ( grantOfRepresentationData.getOutsideUkGrantCopies() != null ) {
            copies.overseas( grantOfRepresentationData.getOutsideUkGrantCopies() );
        }

        return copies.build();
    }

    protected Will grantOfRepresentationDataToWill(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        WillBuilder will = Will.builder();

        if ( grantOfRepresentationData.getWillHasCodicils() != null ) {
            will.codicils( grantOfRepresentationData.getWillHasCodicils() );
        }
        if ( grantOfRepresentationData.getWillNumberOfCodicils() != null ) {
            will.codicilsNumber( grantOfRepresentationData.getWillNumberOfCodicils() );
        }

        return will.build();
    }

    protected PaAssets grantOfRepresentationDataToPaAssets(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        PaAssetsBuilder paAssets = PaAssets.builder();

        paAssets.assetsoverseas( grantOfRepresentationData.getOutsideUkGrantCopies() == null ? null : grantOfRepresentationData.getOutsideUkGrantCopies() > 0L );

        return paAssets.build();
    }
}
