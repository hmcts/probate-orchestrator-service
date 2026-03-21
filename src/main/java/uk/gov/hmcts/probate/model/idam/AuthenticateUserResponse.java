package uk.gov.hmcts.probate.model.idam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthenticateUserResponse(String code) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String code;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public AuthenticateUserResponse build() {
            return new AuthenticateUserResponse(code);
        }
    }
}
