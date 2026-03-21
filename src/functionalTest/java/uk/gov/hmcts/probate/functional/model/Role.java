package uk.gov.hmcts.probate.functional.model;

public record Role(String code) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String code;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Role build() {
            return new Role(code);
        }
    }
}
