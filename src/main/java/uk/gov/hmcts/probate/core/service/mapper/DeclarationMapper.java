package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.Declaration;

public class DeclarationMapper {

    public static Boolean getDeclarationCheckbox(Declaration declaration) {
        if (declaration == null) {
            return null; //NOSONAR
        }
        return declaration.getDeclarationCheckbox();
    }
}
