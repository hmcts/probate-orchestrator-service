package uk.gov.hmcts.probate.dto.formdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DeceasedDTO implements Serializable {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @JsonProperty("dob_date")
    private LocalDate dateOfBirth;

    @NotNull
    @JsonProperty("dod_date")
    private LocalDate dateOfDeath;

    @NotNull
    private String domicile;

    @NotNull
    private String address;

}
