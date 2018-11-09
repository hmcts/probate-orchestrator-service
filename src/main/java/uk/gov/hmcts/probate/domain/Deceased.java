package uk.gov.hmcts.probate.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Deceased {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private LocalDate dateOfDeath;

    private String domicile;

    private String address;
}
