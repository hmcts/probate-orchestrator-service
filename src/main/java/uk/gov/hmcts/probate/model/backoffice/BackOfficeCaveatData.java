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
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
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

        public Builder registryLocation(String registryLocation) {
            this.registryLocation = registryLocation;
            return this;
        }

        public Builder deceasedForenames(String deceasedForenames) {
            this.deceasedForenames = deceasedForenames;
            return this;
        }

        public Builder deceasedSurname(String deceasedSurname) {
            this.deceasedSurname = deceasedSurname;
            return this;
        }

        public Builder deceasedDateOfDeath(String deceasedDateOfDeath) {
            this.deceasedDateOfDeath = deceasedDateOfDeath;
            return this;
        }

        public Builder deceasedDateOfBirth(String deceasedDateOfBirth) {
            this.deceasedDateOfBirth = deceasedDateOfBirth;
            return this;
        }

        public Builder deceasedAnyOtherNames(String deceasedAnyOtherNames) {
            this.deceasedAnyOtherNames = deceasedAnyOtherNames;
            return this;
        }

        public Builder deceasedFullAliasNameList(List<CollectionMember<FullAliasName>> deceasedFullAliasNameList) {
            this.deceasedFullAliasNameList = deceasedFullAliasNameList;
            return this;
        }

        public Builder deceasedAddress(Address deceasedAddress) {
            this.deceasedAddress = deceasedAddress;
            return this;
        }

        public Builder caveatorForenames(String caveatorForenames) {
            this.caveatorForenames = caveatorForenames;
            return this;
        }

        public Builder caveatorSurname(String caveatorSurname) {
            this.caveatorSurname = caveatorSurname;
            return this;
        }

        public Builder caveatorEmailAddress(String caveatorEmailAddress) {
            this.caveatorEmailAddress = caveatorEmailAddress;
            return this;
        }

        public Builder caveatorAddress(Address caveatorAddress) {
            this.caveatorAddress = caveatorAddress;
            return this;
        }

        public Builder expiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder applicationSubmittedDate(String applicationSubmittedDate) {
            this.applicationSubmittedDate = applicationSubmittedDate;
            return this;
        }

        public Builder messageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        public Builder caveatReopenReason(String caveatReopenReason) {
            this.caveatReopenReason = caveatReopenReason;
            return this;
        }

        public Builder caveatRaisedEmailNotificationRequested(String caveatRaisedEmailNotificationRequested) {
            this.caveatRaisedEmailNotificationRequested = caveatRaisedEmailNotificationRequested;
            return this;
        }

        public Builder caveatRaisedEmailNotification(String caveatRaisedEmailNotification) {
            this.caveatRaisedEmailNotification = caveatRaisedEmailNotification;
            return this;
        }

        public Builder notificationsGenerated(List<CollectionMember<ProbateDocument>> notificationsGenerated) {
            this.notificationsGenerated = notificationsGenerated;
            return this;
        }

        public Builder recordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public Builder legacyType(String legacyType) {
            this.legacyType = legacyType;
            return this;
        }

        public Builder legacyCaseViewUrl(String legacyCaseViewUrl) {
            this.legacyCaseViewUrl = legacyCaseViewUrl;
            return this;
        }

        public BackOfficeCaveatData build() {
            return new BackOfficeCaveatData(
                registryLocation,
                deceasedForenames,
                deceasedSurname,
                deceasedDateOfDeath,
                deceasedDateOfBirth,
                deceasedAnyOtherNames,
                deceasedFullAliasNameList,
                deceasedAddress,
                caveatorForenames,
                caveatorSurname,
                caveatorEmailAddress,
                caveatorAddress,
                expiryDate,
                applicationSubmittedDate,
                messageContent,
                caveatReopenReason,
                caveatRaisedEmailNotificationRequested,
                caveatRaisedEmailNotification,
                notificationsGenerated,
                recordId,
                legacyType,
                legacyCaseViewUrl
            );
        }
    }
}
