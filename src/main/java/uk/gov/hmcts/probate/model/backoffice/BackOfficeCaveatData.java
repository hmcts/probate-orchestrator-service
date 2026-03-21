package uk.gov.hmcts.probate.model.backoffice;

import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;

import java.util.List;

public record BackOfficeCaveatData(
    String registryLocation,
    String deceasedForenames,
    String deceasedSurname,
    String deceasedDateOfDeath,
    String deceasedDateOfBirth,
    String deceasedAnyOtherNames,
    List<CollectionMember<FullAliasName>> deceasedFullAliasNameList,
    Address deceasedAddress,
    String caveatorForenames,
    String caveatorSurname,
    String caveatorEmailAddress,
    Address caveatorAddress,
    String expiryDate,
    String applicationSubmittedDate,
    String messageContent,
    String caveatReopenReason,
    String caveatRaisedEmailNotificationRequested,
    String caveatRaisedEmailNotification,
    List<CollectionMember<ProbateDocument>> notificationsGenerated,
    String recordId,
    String legacyType,
    String legacyCaseViewUrl
) {}
