package uk.gov.hmcts.probate.dto.formdata;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@JsonRootName(value = "applicant")
public class ApplicantDTO implements Serializable {

    @NotBlank
    @Size(min = 2, message = "fieldMinSize")
    private String firstName;

    @NotBlank
    @Size(min = 2, message = "fieldMinSize")
    private String lastName;

    @NotBlank
    @Size(min = 2, message = "fieldMinSize")
    private String address;

    private DeclarationDTO declaration;
}
