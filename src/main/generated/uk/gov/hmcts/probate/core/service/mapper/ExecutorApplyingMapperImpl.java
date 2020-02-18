package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying.ExecutorApplyingBuilder;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor.ExecutorBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-18T10:16:52+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class ExecutorApplyingMapperImpl implements ExecutorApplyingMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public CollectionMember<ExecutorApplying> toExecutorApplying(Executor executor) {
        if ( executor == null ) {
            return null;
        }

        CollectionMember<ExecutorApplying> collectionMember = new CollectionMember<ExecutorApplying>();

        collectionMember.setValue( executorToExecutorApplying( executor ) );

        return collectionMember;
    }

    @Override
    public Executor fromExecutorApplying(CollectionMember<ExecutorApplying> executorApplyingCollectionMember) {
        if ( executorApplyingCollectionMember == null ) {
            return null;
        }

        ExecutorBuilder executor = Executor.builder();

        executor.lastName( executorApplyingCollectionMemberValueApplyingExecutorLastName( executorApplyingCollectionMember ) );
        executor.address( addressMapper.toFormAddress( executorApplyingCollectionMemberValueApplyingExecutorAddress( executorApplyingCollectionMember ) ) );
        executor.hasOtherName( executorApplyingCollectionMemberValueApplyingExecutorHasOtherName( executorApplyingCollectionMember ) );
        executor.inviteId( executorApplyingCollectionMemberValueApplyingExecutorInvitationId( executorApplyingCollectionMember ) );
        executor.mobile( executorApplyingCollectionMemberValueApplyingExecutorPhoneNumber( executorApplyingCollectionMember ) );
        executor.postcode( executorApplyingCollectionMemberValueApplyingExecutorPostCode( executorApplyingCollectionMember ) );
        executor.fullName( executorApplyingCollectionMemberValueApplyingExecutorName( executorApplyingCollectionMember ) );
        executor.leadExecutorName( executorApplyingCollectionMemberValueApplyingExecutorLeadName( executorApplyingCollectionMember ) );
        executor.executorAgreed( executorApplyingCollectionMemberValueApplyingExecutorAgreed( executorApplyingCollectionMember ) );
        executor.firstName( executorApplyingCollectionMemberValueApplyingExecutorFirstName( executorApplyingCollectionMember ) );
        executor.otherReason( executorApplyingCollectionMemberValueApplyingExecutorOtherReason( executorApplyingCollectionMember ) );
        executor.isApplicant( executorApplyingCollectionMemberValueApplyingExecutorApplicant( executorApplyingCollectionMember ) );
        executor.currentName( executorApplyingCollectionMemberValueApplyingExecutorOtherNames( executorApplyingCollectionMember ) );
        executor.email( executorApplyingCollectionMemberValueApplyingExecutorEmail( executorApplyingCollectionMember ) );

        executor.currentNameReason( executorApplyingCollectionMember.getValue().getApplyingExecutorOtherNamesReason()!=null ? executorApplyingCollectionMember.getValue().getApplyingExecutorOtherNamesReason().getDescription() : null );
        executor.isApplying( true );

        return executor.build();
    }

    protected ExecutorApplying executorToExecutorApplying(Executor executor) {
        if ( executor == null ) {
            return null;
        }

        ExecutorApplyingBuilder executorApplying = ExecutorApplying.builder();

        executorApplying.applyingExecutorInvitationId( executor.getInviteId() );
        executorApplying.applyingExecutorAgreed( executor.getExecutorAgreed() );
        executorApplying.applyingExecutorAddress( addressMapper.toCaseAddress( executor.getAddress() ) );
        executorApplying.applyingExecutorFirstName( executor.getFirstName() );
        executorApplying.applyingExecutorPhoneNumber( executor.getMobile() );
        executorApplying.applyingExecutorHasOtherName( executor.getHasOtherName() );
        executorApplying.applyingExecutorLastName( executor.getLastName() );
        executorApplying.applyingExecutorApplicant( executor.getIsApplicant() );
        executorApplying.applyingExecutorEmail( executor.getEmail() );
        executorApplying.applyingExecutorPostCode( executor.getPostcode() );
        executorApplying.applyingExecutorLeadName( executor.getLeadExecutorName() );

        executorApplying.applyingExecutorOtherReason( BooleanUtils.isTrue(executor.getHasOtherName()) ? executor.getOtherReason() : null );
        executorApplying.applyingExecutorOtherNames( BooleanUtils.isTrue(executor.getHasOtherName()) ? executor.getCurrentName() : null );
        executorApplying.applyingExecutorName( ExecutorNamesMapper.getFullname(executor) );
        executorApplying.applyingExecutorOtherNamesReason( BooleanUtils.isTrue(executor.getHasOtherName()) ? AliasReason.fromString(executor.getCurrentNameReason()) : null );

        return executorApplying.build();
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorLastName(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorLastName = value.getApplyingExecutorLastName();
        if ( applyingExecutorLastName == null ) {
            return null;
        }
        return applyingExecutorLastName;
    }

    private Address executorApplyingCollectionMemberValueApplyingExecutorAddress(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Address applyingExecutorAddress = value.getApplyingExecutorAddress();
        if ( applyingExecutorAddress == null ) {
            return null;
        }
        return applyingExecutorAddress;
    }

    private Boolean executorApplyingCollectionMemberValueApplyingExecutorHasOtherName(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean applyingExecutorHasOtherName = value.getApplyingExecutorHasOtherName();
        if ( applyingExecutorHasOtherName == null ) {
            return null;
        }
        return applyingExecutorHasOtherName;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorInvitationId(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorInvitationId = value.getApplyingExecutorInvitationId();
        if ( applyingExecutorInvitationId == null ) {
            return null;
        }
        return applyingExecutorInvitationId;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorPhoneNumber(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorPhoneNumber = value.getApplyingExecutorPhoneNumber();
        if ( applyingExecutorPhoneNumber == null ) {
            return null;
        }
        return applyingExecutorPhoneNumber;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorPostCode(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorPostCode = value.getApplyingExecutorPostCode();
        if ( applyingExecutorPostCode == null ) {
            return null;
        }
        return applyingExecutorPostCode;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorName(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorName = value.getApplyingExecutorName();
        if ( applyingExecutorName == null ) {
            return null;
        }
        return applyingExecutorName;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorLeadName(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorLeadName = value.getApplyingExecutorLeadName();
        if ( applyingExecutorLeadName == null ) {
            return null;
        }
        return applyingExecutorLeadName;
    }

    private Boolean executorApplyingCollectionMemberValueApplyingExecutorAgreed(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean applyingExecutorAgreed = value.getApplyingExecutorAgreed();
        if ( applyingExecutorAgreed == null ) {
            return null;
        }
        return applyingExecutorAgreed;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorFirstName(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorFirstName = value.getApplyingExecutorFirstName();
        if ( applyingExecutorFirstName == null ) {
            return null;
        }
        return applyingExecutorFirstName;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorOtherReason(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorOtherReason = value.getApplyingExecutorOtherReason();
        if ( applyingExecutorOtherReason == null ) {
            return null;
        }
        return applyingExecutorOtherReason;
    }

    private Boolean executorApplyingCollectionMemberValueApplyingExecutorApplicant(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean applyingExecutorApplicant = value.getApplyingExecutorApplicant();
        if ( applyingExecutorApplicant == null ) {
            return null;
        }
        return applyingExecutorApplicant;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorOtherNames(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorOtherNames = value.getApplyingExecutorOtherNames();
        if ( applyingExecutorOtherNames == null ) {
            return null;
        }
        return applyingExecutorOtherNames;
    }

    private String executorApplyingCollectionMemberValueApplyingExecutorEmail(CollectionMember<ExecutorApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String applyingExecutorEmail = value.getApplyingExecutorEmail();
        if ( applyingExecutorEmail == null ) {
            return null;
        }
        return applyingExecutorEmail;
    }
}
