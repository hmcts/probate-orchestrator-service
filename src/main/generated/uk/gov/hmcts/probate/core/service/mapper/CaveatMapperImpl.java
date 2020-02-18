package uk.gov.hmcts.probate.core.service.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.Address.AddressBuilder;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData.CaveatDataBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.Language.LanguageBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Registry.RegistryBuilder;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatApplicant;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatApplicant.CaveatApplicantBuilder;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatDeceased;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatDeceased.CaveatDeceasedBuilder;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm.CaveatFormBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-18T10:16:51+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class CaveatMapperImpl implements CaveatMapper {

    @Autowired
    private PaymentsMapper paymentsMapper;
    @Autowired
    private CaveatAliasNameMapper caveatAliasNameMapper;
    @Autowired
    private RegistryLocationMapper registryLocationMapper;

    @Override
    public CaveatData toCaseData(CaveatForm form) {
        if ( form == null ) {
            return null;
        }

        CaveatDataBuilder caveatData = CaveatData.builder();

        Address address = formApplicantAddress( form );
        if ( address != null ) {
            caveatData.caveatorAddress( addressToAddress( address ) );
        }
        Address address1 = formDeceasedAddress( form );
        if ( address1 != null ) {
            caveatData.deceasedAddress( addressToAddress( address1 ) );
        }
        List<CollectionMember<CasePayment>> list = paymentsMapper.toCasePaymentCollectionMembers( form.getPayments() );
        if ( list != null ) {
            caveatData.payments( list );
        }
        LocalDate dateOfBirth = formDeceasedDateOfBirth( form );
        if ( dateOfBirth != null ) {
            caveatData.deceasedDateOfBirth( dateOfBirth );
        }
        String firstName = formDeceasedFirstName( form );
        if ( firstName != null ) {
            caveatData.deceasedForenames( firstName );
        }
        if ( form.getExpiryDate() != null ) {
            caveatData.expiryDate( form.getExpiryDate() );
        }
        LocalDate dateOfDeath = formDeceasedDateOfDeath( form );
        if ( dateOfDeath != null ) {
            caveatData.deceasedDateOfDeath( dateOfDeath );
        }
        Boolean bilingual = formLanguageBilingual( form );
        if ( bilingual != null ) {
            caveatData.languagePreferenceWelsh( bilingual );
        }
        String name = formRegistryName( form );
        if ( name != null ) {
            caveatData.registryLocation( registryLocationMapper.toRegistryLocation( name ) );
        }
        String firstName1 = formApplicantFirstName( form );
        if ( firstName1 != null ) {
            caveatData.caveatorForenames( firstName1 );
        }
        if ( form.getApplicationId() != null ) {
            caveatData.applicationId( form.getApplicationId() );
        }
        String lastName = formApplicantLastName( form );
        if ( lastName != null ) {
            caveatData.caveatorSurname( lastName );
        }
        String lastName1 = formDeceasedLastName( form );
        if ( lastName1 != null ) {
            caveatData.deceasedSurname( lastName1 );
        }
        Map<String, AliasOtherNames> otherNames = formDeceasedOtherNames( form );
        List<CollectionMember<FullAliasName>> list1 = caveatAliasNameMapper.toCaveatCollectionMember( otherNames );
        if ( list1 != null ) {
            caveatData.deceasedFullAliasNameList( list1 );
        }

        caveatData.applicationType( ApplicationType.PERSONAL );
        caveatData.caveatorEmailAddress( form.getApplicant() != null && form.getApplicant().getEmail() != null ? form.getApplicant().getEmail().toLowerCase() : null );
        caveatData.caveatRaisedEmailNotificationRequested( Boolean.TRUE );
        caveatData.paperForm( Boolean.FALSE );

        return caveatData.build();
    }

    @Override
    public CaveatForm fromCaseData(CaveatData caveatdata) {
        if ( caveatdata == null ) {
            return null;
        }

        CaveatFormBuilder caveatForm = CaveatForm.builder();

        caveatForm.deceased( caveatDataToCaveatDeceased( caveatdata ) );
        caveatForm.registry( caveatDataToRegistry( caveatdata ) );
        caveatForm.applicant( caveatDataToCaveatApplicant( caveatdata ) );
        caveatForm.language( caveatDataToLanguage( caveatdata ) );
        List<Payment> list = paymentsMapper.fromCasePaymentCollectionMembers( caveatdata.getPayments() );
        if ( list != null ) {
            caveatForm.payments( list );
        }
        if ( caveatdata.getExpiryDate() != null ) {
            caveatForm.expiryDate( caveatdata.getExpiryDate() );
        }
        if ( caveatdata.getApplicationId() != null ) {
            caveatForm.applicationId( caveatdata.getApplicationId() );
        }

        caveatForm.type( ProbateType.CAVEAT );

        return caveatForm.build();
    }

    private Address formApplicantAddress(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatApplicant applicant = caveatForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        Address address = applicant.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    protected uk.gov.hmcts.reform.probate.model.cases.Address addressToAddress(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressBuilder address1 = uk.gov.hmcts.reform.probate.model.cases.Address.builder();

        if ( address.getAddressLine1() != null ) {
            address1.addressLine1( address.getAddressLine1() );
        }
        if ( address.getAddressLine2() != null ) {
            address1.addressLine2( address.getAddressLine2() );
        }
        if ( address.getAddressLine3() != null ) {
            address1.addressLine3( address.getAddressLine3() );
        }
        if ( address.getCounty() != null ) {
            address1.county( address.getCounty() );
        }
        if ( address.getPostTown() != null ) {
            address1.postTown( address.getPostTown() );
        }
        if ( address.getPostCode() != null ) {
            address1.postCode( address.getPostCode() );
        }
        if ( address.getCountry() != null ) {
            address1.country( address.getCountry() );
        }

        return address1.build();
    }

    private Address formDeceasedAddress(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Address address = deceased.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private LocalDate formDeceasedDateOfBirth(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDate dateOfBirth = deceased.getDateOfBirth();
        if ( dateOfBirth == null ) {
            return null;
        }
        return dateOfBirth;
    }

    private String formDeceasedFirstName(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String firstName = deceased.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private LocalDate formDeceasedDateOfDeath(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        LocalDate dateOfDeath = deceased.getDateOfDeath();
        if ( dateOfDeath == null ) {
            return null;
        }
        return dateOfDeath;
    }

    private Boolean formLanguageBilingual(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        Language language = caveatForm.getLanguage();
        if ( language == null ) {
            return null;
        }
        Boolean bilingual = language.getBilingual();
        if ( bilingual == null ) {
            return null;
        }
        return bilingual;
    }

    private String formRegistryName(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        Registry registry = caveatForm.getRegistry();
        if ( registry == null ) {
            return null;
        }
        String name = registry.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String formApplicantFirstName(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatApplicant applicant = caveatForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String firstName = applicant.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String formApplicantLastName(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatApplicant applicant = caveatForm.getApplicant();
        if ( applicant == null ) {
            return null;
        }
        String lastName = applicant.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String formDeceasedLastName(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        String lastName = deceased.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private Map<String, AliasOtherNames> formDeceasedOtherNames(CaveatForm caveatForm) {
        if ( caveatForm == null ) {
            return null;
        }
        CaveatDeceased deceased = caveatForm.getDeceased();
        if ( deceased == null ) {
            return null;
        }
        Map<String, AliasOtherNames> otherNames = deceased.getOtherNames();
        if ( otherNames == null ) {
            return null;
        }
        return otherNames;
    }

    protected Address addressToAddress1(uk.gov.hmcts.reform.probate.model.cases.Address address) {
        if ( address == null ) {
            return null;
        }

        uk.gov.hmcts.reform.probate.model.forms.Address.AddressBuilder address1 = Address.builder();

        if ( address.getAddressLine1() != null ) {
            address1.addressLine1( address.getAddressLine1() );
        }
        if ( address.getAddressLine2() != null ) {
            address1.addressLine2( address.getAddressLine2() );
        }
        if ( address.getAddressLine3() != null ) {
            address1.addressLine3( address.getAddressLine3() );
        }
        if ( address.getCounty() != null ) {
            address1.county( address.getCounty() );
        }
        if ( address.getPostTown() != null ) {
            address1.postTown( address.getPostTown() );
        }
        if ( address.getPostCode() != null ) {
            address1.postCode( address.getPostCode() );
        }
        if ( address.getCountry() != null ) {
            address1.country( address.getCountry() );
        }

        return address1.build();
    }

    protected CaveatDeceased caveatDataToCaveatDeceased(CaveatData caveatData) {
        if ( caveatData == null ) {
            return null;
        }

        CaveatDeceasedBuilder caveatDeceased = CaveatDeceased.builder();

        if ( caveatData.getDeceasedDateOfBirth() != null ) {
            caveatDeceased.dateOfBirth( caveatData.getDeceasedDateOfBirth() );
        }
        if ( caveatData.getDeceasedForenames() != null ) {
            caveatDeceased.firstName( caveatData.getDeceasedForenames() );
        }
        if ( caveatData.getDeceasedAddress() != null ) {
            caveatDeceased.address( addressToAddress1( caveatData.getDeceasedAddress() ) );
        }
        Map<String, AliasOtherNames> map = caveatAliasNameMapper.fromCaveatCollectionMember( caveatData.getDeceasedFullAliasNameList() );
        if ( map != null ) {
            caveatDeceased.otherNames( map );
        }
        if ( caveatData.getDeceasedDateOfDeath() != null ) {
            caveatDeceased.dateOfDeath( caveatData.getDeceasedDateOfDeath() );
        }
        if ( caveatData.getDeceasedSurname() != null ) {
            caveatDeceased.lastName( caveatData.getDeceasedSurname() );
        }

        return caveatDeceased.build();
    }

    protected Registry caveatDataToRegistry(CaveatData caveatData) {
        if ( caveatData == null ) {
            return null;
        }

        RegistryBuilder registry = Registry.builder();

        if ( caveatData.getRegistryLocation() != null ) {
            registry.name( registryLocationMapper.fromRegistryLocation( caveatData.getRegistryLocation() ) );
        }

        return registry.build();
    }

    protected CaveatApplicant caveatDataToCaveatApplicant(CaveatData caveatData) {
        if ( caveatData == null ) {
            return null;
        }

        CaveatApplicantBuilder caveatApplicant = CaveatApplicant.builder();

        if ( caveatData.getCaveatorSurname() != null ) {
            caveatApplicant.lastName( caveatData.getCaveatorSurname() );
        }
        if ( caveatData.getCaveatorForenames() != null ) {
            caveatApplicant.firstName( caveatData.getCaveatorForenames() );
        }
        if ( caveatData.getCaveatorEmailAddress() != null ) {
            caveatApplicant.email( caveatData.getCaveatorEmailAddress() );
        }
        if ( caveatData.getCaveatorAddress() != null ) {
            caveatApplicant.address( addressToAddress1( caveatData.getCaveatorAddress() ) );
        }

        return caveatApplicant.build();
    }

    protected Language caveatDataToLanguage(CaveatData caveatData) {
        if ( caveatData == null ) {
            return null;
        }

        LanguageBuilder language = Language.builder();

        if ( caveatData.getLanguagePreferenceWelsh() != null ) {
            language.bilingual( caveatData.getLanguagePreferenceWelsh() );
        }

        return language.build();
    }
}
