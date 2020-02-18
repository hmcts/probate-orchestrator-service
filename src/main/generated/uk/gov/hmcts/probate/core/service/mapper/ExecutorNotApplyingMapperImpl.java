package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying.ExecutorNotApplyingBuilder;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor.ExecutorBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-13T11:05:20+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class ExecutorNotApplyingMapperImpl implements ExecutorNotApplyingMapper {

    @Override
    public CollectionMember<ExecutorNotApplying> toExecutorNotApplying(Executor executor) {
        if ( executor == null ) {
            return null;
        }

        CollectionMember<ExecutorNotApplying> collectionMember = new CollectionMember<ExecutorNotApplying>();

        collectionMember.setValue( executorToExecutorNotApplying( executor ) );

        return collectionMember;
    }

    @Override
    public Executor fromExecutorNotApplying(CollectionMember<ExecutorNotApplying> executorNotApplyingCollectionMember) {
        if ( executorNotApplyingCollectionMember == null ) {
            return null;
        }

        ExecutorBuilder executor = Executor.builder();

        executor.diedBefore( executorNotApplyingCollectionMemberValueNotApplyingExecutorDiedBefore( executorNotApplyingCollectionMember ) );
        executor.executorNotified( executorNotApplyingCollectionMemberValueNotApplyingExecutorNotified( executorNotApplyingCollectionMember ) );
        executor.fullName( executorNotApplyingCollectionMemberValueNotApplyingExecutorName( executorNotApplyingCollectionMember ) );
        executor.isDead( executorNotApplyingCollectionMemberValueNotApplyingExecutorIsDead( executorNotApplyingCollectionMember ) );

        executor.notApplyingKey( executorNotApplyingCollectionMember.getValue().getNotApplyingExecutorReason() == null ? null : executorNotApplyingCollectionMember.getValue().getNotApplyingExecutorReason().getOptionValue() );
        executor.isApplying( false );

        return executor.build();
    }

    protected ExecutorNotApplying executorToExecutorNotApplying(Executor executor) {
        if ( executor == null ) {
            return null;
        }

        ExecutorNotApplyingBuilder executorNotApplying = ExecutorNotApplying.builder();

        executorNotApplying.notApplyingExecutorDiedBefore( executor.getDiedBefore() );
        executorNotApplying.notApplyingExecutorIsDead( executor.getIsDead() );
        executorNotApplying.notApplyingExecutorNotified( executor.getExecutorNotified() );

        executorNotApplying.notApplyingExecutorName( ExecutorNamesMapper.getFullname(executor) );
        executorNotApplying.notApplyingExecutorReason( executor.getNotApplyingKey() == null? null : ExecutorNotApplyingReason.getExecutorNotApplyingReasonByValue(executor.getNotApplyingKey()) );

        return executorNotApplying.build();
    }

    private Boolean executorNotApplyingCollectionMemberValueNotApplyingExecutorDiedBefore(CollectionMember<ExecutorNotApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorNotApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean notApplyingExecutorDiedBefore = value.getNotApplyingExecutorDiedBefore();
        if ( notApplyingExecutorDiedBefore == null ) {
            return null;
        }
        return notApplyingExecutorDiedBefore;
    }

    private Boolean executorNotApplyingCollectionMemberValueNotApplyingExecutorNotified(CollectionMember<ExecutorNotApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorNotApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean notApplyingExecutorNotified = value.getNotApplyingExecutorNotified();
        if ( notApplyingExecutorNotified == null ) {
            return null;
        }
        return notApplyingExecutorNotified;
    }

    private String executorNotApplyingCollectionMemberValueNotApplyingExecutorName(CollectionMember<ExecutorNotApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorNotApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        String notApplyingExecutorName = value.getNotApplyingExecutorName();
        if ( notApplyingExecutorName == null ) {
            return null;
        }
        return notApplyingExecutorName;
    }

    private Boolean executorNotApplyingCollectionMemberValueNotApplyingExecutorIsDead(CollectionMember<ExecutorNotApplying> collectionMember) {
        if ( collectionMember == null ) {
            return null;
        }
        ExecutorNotApplying value = collectionMember.getValue();
        if ( value == null ) {
            return null;
        }
        Boolean notApplyingExecutorIsDead = value.getNotApplyingExecutorIsDead();
        if ( notApplyingExecutorIsDead == null ) {
            return null;
        }
        return notApplyingExecutorIsDead;
    }
}
