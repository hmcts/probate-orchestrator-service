package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class OverseasCopiesMapperTest {
    @Test
    void testMapOverseasCopies() {

        PaForm paForm = PaForm.builder().copies(Copies.builder().overseas(3L).build()).build();
        Long result = OverseasCopiesMapper.mapOverseasCopies(paForm);

        assertThat(result, equalTo(3L));

        PaForm paFormA = PaForm.builder().assets(PaAssets.builder().assetsoverseas(Boolean.FALSE).build()).build();
        result = OverseasCopiesMapper.mapOverseasCopies(paFormA);
        assertThat(result, equalTo(0L));
    }
}
