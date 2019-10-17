package uk.gov.hmcts.probate.model.backoffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackOfficeCaveatData {

    private String registryLocation;

    private String deceasedForenames;
    private String deceasedSurname;
    private String deceasedDateOfDeath;
    private String deceasedDateOfBirth;
    private String deceasedAnyOtherNames;
    private List<CollectionMember<FullAliasName>> deceasedFullAliasNameList;
    private Address deceasedAddress;

    private String caveatorForenames;
    private String caveatorSurname;
    private String caveatorEmailAddress;
    private Address caveatorAddress;

    private String expiryDate;
    private String applicationSubmittedDate;
    private String messageContent;
    private String caveatReopenReason;

    private String caveatRaisedEmailNotificationRequested;
    private String caveatRaisedEmailNotification;

    private List<CollectionMember<ProbateDocument>> notificationsGenerated;

    private String recordId;
    private String legacyType;
    private String legacyCaseViewUrl;
}
