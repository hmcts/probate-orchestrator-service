package uk.gov.hmcts.probate.model.idam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenExchangeResponse(@JsonProperty("access_token") String accessToken) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String accessToken;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public TokenExchangeResponse build() {
            return new TokenExchangeResponse(accessToken);
        }
    }
}
