package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;

@Component
public class IhtMethodConverter {

    @ToIhtMethod
    public IhtMethod toIhtMethod(Boolean isOnline) {
        if (isOnline == null) {
            return null;
        }
        return isOnline ? IhtMethod.ONLINE : IhtMethod.BY_POST;
    }

    @FromIhtMethod
    public Boolean fromIhtMethod(IhtMethod ihtMethod) {
        if (ihtMethod == null) {
            return null;
        }
        return IhtMethod.ONLINE.equals(ihtMethod);
    }
}
