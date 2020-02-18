package uk.gov.hmcts.probate.core.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees.ProbateCalculatedFeesBuilder;
import uk.gov.hmcts.reform.probate.model.cases.UploadDocument;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.DeclarationBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData.GrantOfRepresentationDataBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement.LegalStatementBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Copies.CopiesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Fees.FeesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax.InheritanceTaxBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.Language.LanguageBuilder;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Registry.RegistryBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant.IntestacyApplicantBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased.IntestacyDeceasedBuilder;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm.IntestacyFormBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-18T11:21:55+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class IntestacyMapperImpl implements IntestacyMapper {

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
    private MapConverter mapConverter;
    @Autowired
    private LegalStatementMapper legalStatementMapper;
    @Autowired
    private LocalDateTimeMapper localDateTimeMapper;
    @Autowired
    private DocumentsMapper documentsMapper;
    @Autowired
    private StatementOfTruthMapper statementOfTruthMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public GrantOfRepresentationData toCaseData(IntestacyForm form) {
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
        Boolean anyChildren = formDeceasedAnyChildren( form );
        if ( anyChildren != null ) {
            grantOfRepresentationData.deceasedAnyChildren( anyChildren );
        }
        try {
            if ( form.getCheckAnswersSummary() != null ) {
                grantOfRepresentationData.checkAnswersSummaryJson( mapConverter.fromMap( form.getCheckAnswersSummary() ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        LocalDateTime dateOfBirth = formDeceasedDateOfBirth( form );
        if ( dateOfBirth != null ) {
            grantOfRepresentationData.deceasedDateOfBirth( localDateTimeMapper.convertLocalDateTimeToLocalDate( dateOfBirth ) );
        }
        Boolean allDeceasedChildrenOverEighteen = formDeceasedAllDeceasedChildrenOverEighteen( form );
        if ( allDeceasedChildrenOverEighteen != null ) {
            grantOfRepresentationData.allDeceasedChildrenOverEighteen( allDeceasedChildrenOverEighteen );
        }
        if ( form.getStatementOfTruthDocument() != null ) {
            grantOfRepresentationData.statementOfTruthDocument( statementOfTruthMapper.toUploadDocuments( form.getStatementOfTruthDocument() ) );
        }
        LocalDateTime dateOfDeath = formDeceasedDateOfDeath( form );
        if ( dateOfDeath != null ) {
            grantOfRepresentationData.deceasedDateOfDeath( localDateTimeMapper.convertLocalDateTimeToLocalDate( dateOfDeath ) );
        }
        String firstName = formApplicantFirstName( form );
        if ( firstName != null ) {
            grantOfRepresentationData.primaryApplicantForenames( firstName );
        }
        Boolean assetsOutside = formIhtAssetsOutside( form );
        if ( assetsOutside != null ) {
            grantOfRepresentationData.deceasedHasAssetsOutsideUK( assetsOutside );
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
        String identifier = formIhtIdentifier( form );
        if ( identifier != null ) {
            grantOfRepresentationData.ihtReferenceNumber( identifier );
        }
        Boolean anyDeceasedChildrenDieBeforeDeceased = formDeceasedAnyDeceasedChildrenDieBeforeDeceased( form );
        if ( anyDeceasedChildrenDieBeforeDeceased != null ) {
            grantOfRepresentationData.childrenDied( anyDeceasedChildrenDieBeforeDeceased );
        }
        String lastName = formDeceasedLastName( form );
        if ( lastName != null ) {
            grantOfRepresentationData.deceasedSurname( lastName );
        }
        Long sequenceNumber = formRegistrySequenceNumber( form );
        if ( sequenceNumber != null ) {
            grantOfRepresentationData.registrySequenceNumber( sequenceNumber );
        }
        List<CollectionMember<UploadDocument>> list = documentsMapper.toUploadDocuments( form.getDocuments() );
        if ( list != null ) {
            grantOfRepresentationData.boDocumentsUploaded( list );
        }
        Boolean otherChildren = formDeceasedOtherChildren( form );
        if ( otherChildren != null ) {
            grantOfRepresentationData.deceasedOtherChildren( otherChildren );
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
        LegalStatement legalStatement = formDeclarationLegalStatement( form );
        if ( legalStatement != null ) {
            grantOfRepresentationData.legalStatement( legalStatementToLegalStatement( legalStatement ) );
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
        Boolean alias = formDeceasedAlias( form );
        if ( alias != null ) {
            grantOfRepresentationData.deceasedAnyOtherNames( alias );
        }
        List<CollectionMember<CasePayment>> list1 = paPaymentMapper.paymentToCasePaymentCollectionMembers( form.getPayment() );
        if ( list1 != null ) {
            grantOfRepresentationData.payments( list1 );
        }
        try {
            if ( form.getLegalDeclaration() != null ) {
                grantOfRepresentationData.legalDeclarationJson( mapConverter.fromMap( form.getLegalDeclaration() ) );
            }
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        String email = formRegistryEmail( form );
        if ( email != null ) {
            grantOfRepresentationData.registryEmailAddress( email );
        }
        Address address1 = formApplicantAddress( form );
        if ( address1 != null ) {
            grantOfRepresentationData.primaryApplicantAddress( addressMapper.toCaseAddress( address1 ) );
        }
        Boolean divorcedInEnglandOrWales = formDeceasedDivorcedInEnglandOrWales( form );
        if ( divorcedInEnglandOrWales != null ) {
            grantOfRepresentationData.deceasedDivorcedInEnglandOrWales( divorcedInEnglandOrWales );
        }
        String phoneNumber = formApplicantPhoneNumber( form );
        if ( phoneNumber != null ) {
            grantOfRepresentationData.primaryApplicantPhoneNumber( phoneNumber );
        }
        BigDecimal netValue = formIhtNetValue( form );
        if ( netValue != null ) {
            grantOfRepresentationData.ihtNetValue( poundsConverter.poundsToPennies( netValue ) );
        }
        BigDecimal assetsOutsideNetValue = formIhtAssetsOutsideNetValue( form );
        if ( assetsOutsideNetValue != null ) {
            grantOfRepresentationData.assetsOutsideNetValue( poundsConverter.poundsToPennies( assetsOutsideNetValue ) );
        }
        Map<String, AliasOtherNames> otherNames = formDeceasedOtherNames( form );
        List<CollectionMember<AliasName>> list2 = aliasNameMapper.toCollectionMember( otherNames );
        if ( list2 != null ) {
            grantOfRepresentationData.deceasedAliasNameList( list2 );
        }
        Long uk = formCopiesUk( form );
        if ( uk != null ) {
            grantOfRepresentationData.extraCopiesOfGrant( uk );
        }
        String grossValueField = formIhtGrossValueField( form );
        if ( grossValueField != null ) {
            grantOfRepresentationData.ihtGrossValueField( grossValueField );
        }
        String firstName1 = formDeceasedFirstName( form );
        if ( firstName1 != null ) {
            grantOfRepresentationData.deceasedForenames( firstName1 );
        }
        String postcode = formApplicantPostcode( form );
        if ( postcode != null ) {
            grantOfRepresentationData.primaryApplicantPostCode( postcode );
        }
        Boolean adoptionInEnglandOrWales = formApplicantAdoptionInEnglandOrWales( form );
        if ( adoptionInEnglandOrWales != null ) {
            grantOfRepresentationData.primaryApplicantAdoptionInEnglandOrWales( adoptionInEnglandOrWales );
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
        Long overseas = formCopiesOverseas( form );
        if ( overseas != null ) {
            grantOfRepresentationData.outsideUkGrantCopies( overseas );
        }
        Boolean anyDeceasedGrandchildrenUnderEighteen = formDeceasedAnyDeceasedGrandchildrenUnderEighteen( form );
        if ( anyDeceasedGrandchildrenUnderEighteen != null ) {
            grantOfRepresentationData.anyDeceasedGrandChildrenUnderEighteen( anyDeceasedGrandchildrenUnderEighteen );
        }
        if ( form.getDeclaration() != null ) {
            grantOfRepresentationData.declaration( declarationToDeclaration( form.getDeclaration() ) );
        }
        if ( form.getFees() != null ) {
            grantOfRepresentationData.fees( feesToProbateCalculatedFees( form.getFees() ) );
        }

        grantOfRepresentationData.applicationType( ApplicationType.PERSONAL );
        grantOfRepresentationData.deceasedMaritalStatus( form.getDeceased()!= null ? MaritalStatus.fromString(form.getDeceased().getMaritalStatus()) : null );
        grantOfRepresentationData.primaryApplicantRelationshipToDeceased( form.getApplicant()!= null && form.getApplicant().getRelationshipToDeceased() != null ? Relationship.fromString(form.getApplicant().getRelationshipToDeceased()) : null );
        grantOfRepresentationData.grantType( GrantType.INTESTACY );
        grantOfRepresentationData.primaryApplicantEmailAddress( form.getApplicantEmail() != null ? form.getApplicantEmail().toLowerCase() : null );
        grantOfRepresentationData.applicationSubmittedDate( LocalDate.now() );
        grantOfRepresentationData.deceasedSpouseNotApplyingReason( form.getApplicant()!= null && form.getApplicant().getSpouseNotApplyingReason() != null ? SpouseNotApplyingReason.fromString(form.getApplicant().getSpouseNotApplyingReason()) : null );

        return grantOfRepresentationData.build();
    }

    @Override
    public IntestacyForm fromCaseData(GrantOfRepresentationData grantOfRepresentation) {
        if ( grantOfRepresentation == null ) {
            return null;
        }

        IntestacyFormBuilder intestacyForm = IntestacyForm.builder();

        intestacyForm.registry( grantOfRepresentationDataToRegistry( grantOfRepresentation ) );
        intestacyForm.deceased( grantOfRepresentationDataToIntestacyDeceased( grantOfRepresentation ) );
        intestacyForm.language( grantOfRepresentationDataToLanguage( grantOfRepresentation ) );
        intestacyForm.iht( grantOfRepresentationDataToInheritanceTax( grantOfRepresentation ) );
        intestacyForm.applicant( grantOfRepresentationDataToIntestacyApplicant( grantOfRepresentation ) );
        intestacyForm.declaration( grantOfRepresentationDataToDeclaration( grantOfRepresentation ) );
        intestacyForm.copies( grantOfRepresentationDataToCopies( grantOfRepresentation ) );
        try {
            Map<String, Object> map = mapConverter.toMap( grantOfRepresentation.getCheckAnswersSummaryJson() );
            if ( map != null ) {
                intestacyForm.checkAnswersSummary( map );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentation.getBoDocumentsUploaded() != null ) {
            intestacyForm.documents( documentsMapper.fromUploadDocs( grantOfRepresentation.getBoDocumentsUploaded() ) );
        }
        if ( grantOfRepresentation.getStatementOfTruthDocument() != null ) {
            intestacyForm.statementOfTruthDocument( statementOfTruthMapper.fromDocumentLink( grantOfRepresentation.getStatementOfTruthDocument() ) );
        }
        if ( grantOfRepresentation.getPayments() != null ) {
            intestacyForm.payment( paPaymentMapper.paymentFromCasePaymentCollectionMembers( grantOfRepresentation.getPayments() ) );
        }
        List<Payment> list = paymentsMapper.fromCasePaymentCollectionMembers( grantOfRepresentation.getPayments() );
        if ( list != null ) {
            intestacyForm.payments( list );
        }
        try {
            Map<String, Object> map1 = mapConverter.toMap( grantOfRepresentation.getLegalDeclarationJson() );
            if ( map1 != null ) {
                intestacyForm.legalDeclaration( map1 );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentation.getFees() != null ) {
            intestacyForm.fees( probateCalculatedFeesToFees( grantOfRepresentation.getFees() ) );
        }
        if ( grantOfRepresentation.getApplicationSubmittedDate() != null ) {
            intestacyForm.applicationSubmittedDate( grantOfRepresentation.getApplicationSubmittedDate() );
        }

        intestacyForm.type( ProbateType.INTESTACY );
        intestacyForm.caseType( GrantType.INTESTACY.getName() );

        return intestacyForm.build();
    }

    private String formIhtNetValueField(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String netValueField = iht.getNetValueField();
        if ( netValueField == null ) {
            return null;
        }
        return netValueField;
    }

    private Address formDeceasedAddress(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Address address = deceased.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formDeceasedAnyChildren(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean anyChildren = deceased.getAnyChildren();
        if ( anyChildren == null ) {
            return null;
        }
        return anyChildren;
    }

    private LocalDateTime formDeceasedDateOfBirth(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDateTime dateOfBirth = deceased.getDateOfBirth();
        if ( dateOfBirth == null ) {
            return null;
        }
        return dateOfBirth;
    }

    private Boolean formDeceasedAllDeceasedChildrenOverEighteen(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean allDeceasedChildrenOverEighteen = deceased.getAllDeceasedChildrenOverEighteen();
        if ( allDeceasedChildrenOverEighteen == null ) {
            return null;
        }
        return allDeceasedChildrenOverEighteen;
    }

    private LocalDateTime formDeceasedDateOfDeath(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDateTime dateOfDeath = deceased.getDateOfDeath();
        if ( dateOfDeath == null ) {
            return null;
        }
        return dateOfDeath;
    }

    private String formApplicantFirstName(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String firstName = applicant.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private Boolean formIhtAssetsOutside(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        Boolean assetsOutside = iht.getAssetsOutside();
        if ( assetsOutside == null ) {
            return null;
        }
        return assetsOutside;
    }

    private List<Map<String, Object>> formApplicantAddresses(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        List<Map<String, Object>> addresses = applicant.getAddresses();
        if ( addresses == null ) {
            return null;
        }
        return addresses;
    }

    private String formIhtIdentifier(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String identifier = iht.getIdentifier();
        if ( identifier == null ) {
            return null;
        }
        return identifier;
    }

    private Boolean formDeceasedAnyDeceasedChildrenDieBeforeDeceased(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean anyDeceasedChildrenDieBeforeDeceased = deceased.getAnyDeceasedChildrenDieBeforeDeceased();
        if ( anyDeceasedChildrenDieBeforeDeceased == null ) {
            return null;
        }
        return anyDeceasedChildrenDieBeforeDeceased;
    }

    private String formDeceasedLastName(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String lastName = deceased.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private Long formRegistrySequenceNumber(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Registry registry = intestacyForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        Long sequenceNumber = registry.getSequenceNumber();
        if ( sequenceNumber == null ) {
            return null;
        }
        return sequenceNumber;
    }

    private Boolean formDeceasedOtherChildren(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean otherChildren = deceased.getOtherChildren();
        if ( otherChildren == null ) {
            return null;
        }
        return otherChildren;
    }

    private BigDecimal formIhtGrossValue(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        BigDecimal grossValue = iht.getGrossValue();
        if ( grossValue == null ) {
            return null;
        }
        return grossValue;
    }

    private String formRegistryName(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Registry registry = intestacyForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String name = registry.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String formIhtIhtFormId(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String ihtFormId = iht.getIhtFormId();
        if ( ihtFormId == null ) {
            return null;
        }
        return ihtFormId;
    }

    private Boolean formDeclarationDeclarationCheckbox(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Declaration declaration = intestacyForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        Boolean declarationCheckbox = declaration.getDeclarationCheckbox();
        if ( declarationCheckbox == null ) {
            return null;
        }
        return declarationCheckbox;
    }

    private LegalStatement formDeclarationLegalStatement(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Declaration declaration = intestacyForm.getDeclaration();
        if ( declaration == null ) {
            return null;
        }
        LegalStatement legalStatement = declaration.getLegalStatement();
        if ( legalStatement == null ) {
            return null;
        }
        return legalStatement;
    }

    protected uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement legalStatementToLegalStatement(LegalStatement legalStatement) {
        if ( legalStatement == null ) {
            return null;
        }

        LegalStatementBuilder legalStatement1 = uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement.builder();

        return legalStatement1.build();
    }

    private List<Map<String, Object>> formDeceasedAddresses(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        List<Map<String, Object>> addresses = deceased.getAddresses();
        if ( addresses == null ) {
            return null;
        }
        return addresses;
    }

    private Boolean formDeceasedAlias(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean alias = deceased.getAlias();
        if ( alias == null ) {
            return null;
        }
        return alias;
    }

    private String formRegistryEmail(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Registry registry = intestacyForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String email = registry.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private Address formApplicantAddress(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Address address = applicant.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formDeceasedDivorcedInEnglandOrWales(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean divorcedInEnglandOrWales = deceased.getDivorcedInEnglandOrWales();
        if ( divorcedInEnglandOrWales == null ) {
            return null;
        }
        return divorcedInEnglandOrWales;
    }

    private String formApplicantPhoneNumber(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String phoneNumber = applicant.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber;
    }

    private BigDecimal formIhtNetValue(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        BigDecimal netValue = iht.getNetValue();
        if ( netValue == null ) {
            return null;
        }
        return netValue;
    }

    private BigDecimal formIhtAssetsOutsideNetValue(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        BigDecimal assetsOutsideNetValue = iht.getAssetsOutsideNetValue();
        if ( assetsOutsideNetValue == null ) {
            return null;
        }
        return assetsOutsideNetValue;
    }

    private Map<String, AliasOtherNames> formDeceasedOtherNames(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Map<String, AliasOtherNames> otherNames = deceased.getOtherNames();
        if ( otherNames == null ) {
            return null;
        }
        return otherNames;
    }

    private Long formCopiesUk(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Copies copies = intestacyForm.getCopies();
        if ( copies == null ) {
            return null;
        }
        Long uk = copies.getUk();
        if ( uk == null ) {
            return null;
        }
        return uk;
    }

    private String formIhtGrossValueField(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        String grossValueField = iht.getGrossValueField();
        if ( grossValueField == null ) {
            return null;
        }
        return grossValueField;
    }

    private String formDeceasedFirstName(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String firstName = deceased.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String formApplicantPostcode(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String postcode = applicant.getPostcode();
        if ( postcode == null ) {
            return null;
        }
        return postcode;
    }

    private Boolean formApplicantAdoptionInEnglandOrWales(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Boolean adoptionInEnglandOrWales = applicant.getAdoptionInEnglandOrWales();
        if ( adoptionInEnglandOrWales == null ) {
            return null;
        }
        return adoptionInEnglandOrWales;
    }

    private String formRegistryAddress(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Registry registry = intestacyForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String address = registry.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private Boolean formLanguageBilingual(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Language language = intestacyForm.getLanguage();
        if ( language == null ) {
            return null;
        }
        Boolean bilingual = language.getBilingual();
        if ( bilingual == null ) {
            return null;
        }
        return bilingual;
    }

    private String formDeceasedPostcode(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String postcode = deceased.getPostcode();
        if ( postcode == null ) {
            return null;
        }
        return postcode;
    }

    private IhtMethod formIhtMethod(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        InheritanceTax iht = intestacyForm.getIht();
        if ( iht == null ) {
            return null;
        }
        IhtMethod method = iht.getMethod();
        if ( method == null ) {
            return null;
        }
        return method;
    }

    private String formApplicantLastName(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyApplicant applicant = intestacyForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String lastName = applicant.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private Long formCopiesOverseas(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        Copies copies = intestacyForm.getCopies();
        if ( copies == null ) {
            return null;
        }
        Long overseas = copies.getOverseas();
        if ( overseas == null ) {
            return null;
        }
        return overseas;
    }

    private Boolean formDeceasedAnyDeceasedGrandchildrenUnderEighteen(IntestacyForm intestacyForm) {
        if ( intestacyForm == null ) {
            return null;
        }
        IntestacyDeceased deceased = intestacyForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Boolean anyDeceasedGrandchildrenUnderEighteen = deceased.getAnyDeceasedGrandchildrenUnderEighteen();
        if ( anyDeceasedGrandchildrenUnderEighteen == null ) {
            return null;
        }
        return anyDeceasedGrandchildrenUnderEighteen;
    }

    protected uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration declarationToDeclaration(Declaration declaration) {
        if ( declaration == null ) {
            return null;
        }

        DeclarationBuilder declaration1 = uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration.builder();

        return declaration1.build();
    }

    protected ProbateCalculatedFees feesToProbateCalculatedFees(Fees fees) {
        if ( fees == null ) {
            return null;
        }

        ProbateCalculatedFeesBuilder probateCalculatedFees = ProbateCalculatedFees.builder();

        if ( fees.getApplicationFee() != null ) {
            probateCalculatedFees.applicationFee( poundsConverter.poundsToPennies( fees.getApplicationFee() ) );
        }
        if ( fees.getApplicationFeeCode() != null ) {
            probateCalculatedFees.applicationFeeCode( fees.getApplicationFeeCode() );
        }
        if ( fees.getApplicationFeeVersion() != null ) {
            probateCalculatedFees.applicationFeeVersion( fees.getApplicationFeeVersion() );
        }
        if ( fees.getUkCopiesFee() != null ) {
            probateCalculatedFees.ukCopiesFee( poundsConverter.poundsToPennies( fees.getUkCopiesFee() ) );
        }
        if ( fees.getUkCopiesFeeCode() != null ) {
            probateCalculatedFees.ukCopiesFeeCode( fees.getUkCopiesFeeCode() );
        }
        if ( fees.getUkCopiesFeeVersion() != null ) {
            probateCalculatedFees.ukCopiesFeeVersion( fees.getUkCopiesFeeVersion() );
        }
        if ( fees.getOverseasCopiesFee() != null ) {
            probateCalculatedFees.overseasCopiesFee( poundsConverter.poundsToPennies( fees.getOverseasCopiesFee() ) );
        }
        if ( fees.getOverseasCopiesFeeCode() != null ) {
            probateCalculatedFees.overseasCopiesFeeCode( fees.getOverseasCopiesFeeCode() );
        }
        if ( fees.getOverseasCopiesFeeVersion() != null ) {
            probateCalculatedFees.overseasCopiesFeeVersion( fees.getOverseasCopiesFeeVersion() );
        }
        if ( fees.getTotal() != null ) {
            probateCalculatedFees.total( poundsConverter.poundsToPennies( fees.getTotal() ) );
        }

        return probateCalculatedFees.build();
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

    protected IntestacyDeceased grantOfRepresentationDataToIntestacyDeceased(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        IntestacyDeceasedBuilder intestacyDeceased = IntestacyDeceased.builder();

        if ( grantOfRepresentationData.getDeceasedForenames() != null ) {
            intestacyDeceased.firstName( grantOfRepresentationData.getDeceasedForenames() );
        }
        if ( grantOfRepresentationData.getDeceasedDateOfDeath() != null ) {
            intestacyDeceased.dateOfDeath( localDateTimeMapper.convertLocalDateToLocalDateTime( grantOfRepresentationData.getDeceasedDateOfDeath() ) );
        }
        if ( grantOfRepresentationData.getDeceasedSurname() != null ) {
            intestacyDeceased.lastName( grantOfRepresentationData.getDeceasedSurname() );
        }
        if ( grantOfRepresentationData.getAllDeceasedChildrenOverEighteen() != null ) {
            intestacyDeceased.allDeceasedChildrenOverEighteen( grantOfRepresentationData.getAllDeceasedChildrenOverEighteen() );
        }
        if ( grantOfRepresentationData.getDeceasedAddress() != null ) {
            intestacyDeceased.address( addressMapper.toFormAddress( grantOfRepresentationData.getDeceasedAddress() ) );
        }
        Map<String, AliasOtherNames> map = aliasNameMapper.fromCollectionMember( grantOfRepresentationData.getDeceasedAliasNameList() );
        if ( map != null ) {
            intestacyDeceased.otherNames( map );
        }
        if ( grantOfRepresentationData.getDeceasedOtherChildren() != null ) {
            intestacyDeceased.otherChildren( grantOfRepresentationData.getDeceasedOtherChildren() );
        }
        if ( grantOfRepresentationData.getDeceasedAnyChildren() != null ) {
            intestacyDeceased.anyChildren( grantOfRepresentationData.getDeceasedAnyChildren() );
        }
        try {
            List<Map<String, Object>> list = mapConverter.toMapList( grantOfRepresentationData.getDeceasedAddresses() );
            if ( list != null ) {
                intestacyDeceased.addresses( list );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentationData.getDeceasedDivorcedInEnglandOrWales() != null ) {
            intestacyDeceased.divorcedInEnglandOrWales( grantOfRepresentationData.getDeceasedDivorcedInEnglandOrWales() );
        }
        if ( grantOfRepresentationData.getDeceasedPostCode() != null ) {
            intestacyDeceased.postcode( grantOfRepresentationData.getDeceasedPostCode() );
        }
        if ( grantOfRepresentationData.getDeceasedDateOfBirth() != null ) {
            intestacyDeceased.dateOfBirth( localDateTimeMapper.convertLocalDateToLocalDateTime( grantOfRepresentationData.getDeceasedDateOfBirth() ) );
        }
        if ( grantOfRepresentationData.getChildrenDied() != null ) {
            intestacyDeceased.anyDeceasedChildrenDieBeforeDeceased( grantOfRepresentationData.getChildrenDied() );
        }
        if ( grantOfRepresentationData.getDeceasedAnyOtherNames() != null ) {
            intestacyDeceased.alias( grantOfRepresentationData.getDeceasedAnyOtherNames() );
        }
        if ( grantOfRepresentationData.getAnyDeceasedGrandChildrenUnderEighteen() != null ) {
            intestacyDeceased.anyDeceasedGrandchildrenUnderEighteen( grantOfRepresentationData.getAnyDeceasedGrandChildrenUnderEighteen() );
        }

        intestacyDeceased.maritalStatus( grantOfRepresentationData.getDeceasedMaritalStatus()!=null ? grantOfRepresentationData.getDeceasedMaritalStatus().getDescription() : null );

        return intestacyDeceased.build();
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
        if ( grantOfRepresentationData.getAssetsOutsideNetValue() != null ) {
            inheritanceTax.assetsOutsideNetValue( poundsConverter.penniesToPounds( grantOfRepresentationData.getAssetsOutsideNetValue() ) );
        }
        if ( grantOfRepresentationData.getIhtFormId() != null ) {
            inheritanceTax.ihtFormId( grantOfRepresentationData.getIhtFormId().name() );
        }
        if ( grantOfRepresentationData.getIhtGrossValueField() != null ) {
            inheritanceTax.grossValueField( grantOfRepresentationData.getIhtGrossValueField() );
        }
        if ( grantOfRepresentationData.getAssetsOutsideNetValue() != null ) {
            inheritanceTax.netValueAssetsOutsideField( poundsConverter.penniesToPoundsString( grantOfRepresentationData.getAssetsOutsideNetValue() ) );
        }
        if ( grantOfRepresentationData.getIhtNetValueField() != null ) {
            inheritanceTax.netValueField( grantOfRepresentationData.getIhtNetValueField() );
        }
        if ( grantOfRepresentationData.getIhtGrossValue() != null ) {
            inheritanceTax.grossValue( poundsConverter.penniesToPounds( grantOfRepresentationData.getIhtGrossValue() ) );
        }
        if ( grantOfRepresentationData.getDeceasedHasAssetsOutsideUK() != null ) {
            inheritanceTax.assetsOutside( grantOfRepresentationData.getDeceasedHasAssetsOutsideUK() );
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

    protected IntestacyApplicant grantOfRepresentationDataToIntestacyApplicant(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        IntestacyApplicantBuilder intestacyApplicant = IntestacyApplicant.builder();

        if ( grantOfRepresentationData.getPrimaryApplicantPostCode() != null ) {
            intestacyApplicant.postcode( grantOfRepresentationData.getPrimaryApplicantPostCode() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantAdoptionInEnglandOrWales() != null ) {
            intestacyApplicant.adoptionInEnglandOrWales( grantOfRepresentationData.getPrimaryApplicantAdoptionInEnglandOrWales() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantForenames() != null ) {
            intestacyApplicant.firstName( grantOfRepresentationData.getPrimaryApplicantForenames() );
        }
        try {
            List<Map<String, Object>> list = mapConverter.toMapList( grantOfRepresentationData.getPrimaryApplicantAddresses() );
            if ( list != null ) {
                intestacyApplicant.addresses( list );
            }
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantPhoneNumber() != null ) {
            intestacyApplicant.phoneNumber( grantOfRepresentationData.getPrimaryApplicantPhoneNumber() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantSurname() != null ) {
            intestacyApplicant.lastName( grantOfRepresentationData.getPrimaryApplicantSurname() );
        }
        if ( grantOfRepresentationData.getPrimaryApplicantAddress() != null ) {
            intestacyApplicant.address( addressMapper.toFormAddress( grantOfRepresentationData.getPrimaryApplicantAddress() ) );
        }

        intestacyApplicant.relationshipToDeceased( grantOfRepresentationData.getPrimaryApplicantRelationshipToDeceased()!=null ? grantOfRepresentationData.getPrimaryApplicantRelationshipToDeceased().getDescription() : null );
        intestacyApplicant.spouseNotApplyingReason( grantOfRepresentationData.getDeceasedSpouseNotApplyingReason()!=null ? grantOfRepresentationData.getDeceasedSpouseNotApplyingReason().getDescription() : null );

        return intestacyApplicant.build();
    }

    protected LegalStatement legalStatementToLegalStatement1(uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement legalStatement) {
        if ( legalStatement == null ) {
            return null;
        }

        uk.gov.hmcts.reform.probate.model.forms.LegalStatement.LegalStatementBuilder legalStatement1 = LegalStatement.builder();

        return legalStatement1.build();
    }

    protected Declaration grantOfRepresentationDataToDeclaration(GrantOfRepresentationData grantOfRepresentationData) {
        if ( grantOfRepresentationData == null ) {
            return null;
        }

        uk.gov.hmcts.reform.probate.model.forms.Declaration.DeclarationBuilder declaration = Declaration.builder();

        if ( grantOfRepresentationData.getDeclarationCheckbox() != null ) {
            declaration.declarationCheckbox( grantOfRepresentationData.getDeclarationCheckbox() );
        }
        if ( grantOfRepresentationData.getLegalStatement() != null ) {
            declaration.legalStatement( legalStatementToLegalStatement1( grantOfRepresentationData.getLegalStatement() ) );
        }

        return declaration.build();
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

    protected Fees probateCalculatedFeesToFees(ProbateCalculatedFees probateCalculatedFees) {
        if ( probateCalculatedFees == null ) {
            return null;
        }

        FeesBuilder fees = Fees.builder();

        if ( probateCalculatedFees.getApplicationFee() != null ) {
            fees.applicationFee( poundsConverter.penniesToPounds( probateCalculatedFees.getApplicationFee() ) );
        }
        if ( probateCalculatedFees.getApplicationFeeCode() != null ) {
            fees.applicationFeeCode( probateCalculatedFees.getApplicationFeeCode() );
        }
        if ( probateCalculatedFees.getApplicationFeeVersion() != null ) {
            fees.applicationFeeVersion( probateCalculatedFees.getApplicationFeeVersion() );
        }
        if ( probateCalculatedFees.getUkCopiesFee() != null ) {
            fees.ukCopiesFee( poundsConverter.penniesToPounds( probateCalculatedFees.getUkCopiesFee() ) );
        }
        if ( probateCalculatedFees.getUkCopiesFeeCode() != null ) {
            fees.ukCopiesFeeCode( probateCalculatedFees.getUkCopiesFeeCode() );
        }
        if ( probateCalculatedFees.getUkCopiesFeeVersion() != null ) {
            fees.ukCopiesFeeVersion( probateCalculatedFees.getUkCopiesFeeVersion() );
        }
        if ( probateCalculatedFees.getOverseasCopiesFee() != null ) {
            fees.overseasCopiesFee( poundsConverter.penniesToPounds( probateCalculatedFees.getOverseasCopiesFee() ) );
        }
        if ( probateCalculatedFees.getOverseasCopiesFeeCode() != null ) {
            fees.overseasCopiesFeeCode( probateCalculatedFees.getOverseasCopiesFeeCode() );
        }
        if ( probateCalculatedFees.getOverseasCopiesFeeVersion() != null ) {
            fees.overseasCopiesFeeVersion( probateCalculatedFees.getOverseasCopiesFeeVersion() );
        }
        if ( probateCalculatedFees.getTotal() != null ) {
            fees.total( poundsConverter.penniesToPounds( probateCalculatedFees.getTotal() ) );
        }

        return fees.build();
    }
}
