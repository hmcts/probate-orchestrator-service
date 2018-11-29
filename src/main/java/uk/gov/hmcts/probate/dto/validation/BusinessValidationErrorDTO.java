package uk.gov.hmcts.probate.dto.validation;

import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;

public class BusinessValidationErrorDTO implements Serializable {


    private String param;
    private String code;
    private String msg;

    public BusinessValidationErrorDTO() {
        super();
    }

    public BusinessValidationErrorDTO(String param, String code, String msg) {
        this.param = param;
        this.code = code;
        this.msg = msg;
    }

    public BusinessValidationErrorDTO generateError(String param, String code) {
        return new BusinessValidationErrorDTO(param, code, "");
    }


    public String getParam() {
        return param;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
