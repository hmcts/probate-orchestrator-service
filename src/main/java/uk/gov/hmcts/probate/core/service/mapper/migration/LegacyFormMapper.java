package uk.gov.hmcts.probate.core.service.mapper.migration;

import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

public interface LegacyFormMapper {

    GrantOfRepresentationData toCaseData(LegacyForm form);
}
