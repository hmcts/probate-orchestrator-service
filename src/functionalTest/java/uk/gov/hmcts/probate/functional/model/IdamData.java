package uk.gov.hmcts.probate.functional.model;

import java.util.List;

public record IdamData(
    String email,
    String forename,
    String surname,
    String password,
    String userGroup,
    List<Role> roles
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String email;
        private String forename;
        private String surname;
        private String password;
        private String userGroup;
        private List<Role> roles;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder forename(String forename) {
            this.forename = forename;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder userGroup(String userGroup) {
            this.userGroup = userGroup;
            return this;
        }

        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public IdamData build() {
            return new IdamData(email, forename, surname, password, userGroup, roles);
        }
    }
}
