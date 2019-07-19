package uk.gov.hmcts.probate.model.backoffice;

import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;

import java.util.List;

@Data
@Builder
public class BackOfficeCaveatData {

    private final String registryLocation;

    private final String deceasedForenames;
    private final String deceasedSurname;
    private final String deceasedDateOfDeath;
    private final String deceasedDateOfBirth;
    private final String deceasedAnyOtherNames;
    private final List<CollectionMember<FullAliasName>> deceasedFullAliasNameList;
    private final Address deceasedAddress;

    private final String caveatorForenames;
    private final String caveatorSurname;
    private final String caveatorEmailAddress;
    private final Address caveatorAddress;

    private final String expiryDate;
    private final String applicationSubmittedDate;
    private final String messageContent;
    private final String caveatReopenReason;

    private final String caveatRaisedEmailNotificationRequested;
    private final String caveatRaisedEmailNotification;

    private final List<CollectionMember<ProbateDocument>> notificationsGenerated;

    private String recordId;
    private String legacyType;
    private String legacyCaseViewUrl;
}
