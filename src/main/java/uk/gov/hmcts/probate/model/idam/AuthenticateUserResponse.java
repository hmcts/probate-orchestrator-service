package uk.gov.hmcts.probate.model.idam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AuthenticateUserResponse {

    private String code;

    public AuthenticateUserResponse(){
    }

    public AuthenticateUserResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}