package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;

public class IhtMethodConverterTest {

    private IhtMethodConverter ihtMethodConverter = new IhtMethodConverter();

    @Test
    void toIhtMethod() {
        IhtMethod ihtMethod = ihtMethodConverter.toIhtMethod(Boolean.TRUE);
        Assertions.assertThat(ihtMethod).isEqualTo(IhtMethod.ONLINE);
    }

    @Test
    void fromIhtMethod() {
        Boolean result = ihtMethodConverter.fromIhtMethod(IhtMethod.BY_POST);
        Assertions.assertThat(result).isEqualTo(Boolean.FALSE);
    }

    @Test
    void fromIhtMethodNullValuesHandled() {
        Boolean result = ihtMethodConverter.fromIhtMethod(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    void toIhtMethodNullValuesHandled() {
        IhtMethod result = ihtMethodConverter.toIhtMethod(null);
        Assertions.assertThat(result).isNull();
    }
}