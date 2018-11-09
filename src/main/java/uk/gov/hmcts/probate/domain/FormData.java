package uk.gov.hmcts.probate.domain;

import lombok.Data;

@Data
public class FormData {

    private Applicant applicant;

    private Deceased deceased;

    private InheritanceTax iht;

    private String softStop;
}
