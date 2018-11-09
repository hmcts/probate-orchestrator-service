package uk.gov.hmcts.probate.domain;

import lombok.Data;

@Data
public class Applicant {

    private String firstName;

    private String lastName;

    private String address;

    private Declaration declaration;
}
