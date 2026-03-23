package uk.gov.hmcts.probate.functional.model;

import java.util.List;

public record IdamData(
    String email,
    String forename,
    String surname,
    String password,
    String userGroup,
    List<Role> roles
) {}
