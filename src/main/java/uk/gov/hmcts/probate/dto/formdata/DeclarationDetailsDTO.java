package uk.gov.hmcts.probate.dto.formdata;

import lombok.Data;

@Data
public class DeclarationDetailsDTO {

    private String confirm;

    private String confirmItem1;

    private String confirmItem2;

    private String confirmItem3;

    private String requests;

    private String requestsItem1;

    private String requestsItem2;

    private String understand;

    private String understandItem1;

    private String understandItem2;

    private String accept;
}
