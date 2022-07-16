package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
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

        PaForm overseasNull = PaForm.builder().copies(Copies.builder().overseas(null).build()).build();
        Assertions.assertThat(OverseasCopiesMapper.mapOverseasCopies(overseasNull)).isNull();

        PaForm copiesNull = PaForm.builder().copies(null).build();
        Assertions.assertThat(OverseasCopiesMapper.mapOverseasCopies(copiesNull)).isNull();

        PaForm assetsOverseasNull = PaForm.builder().assets(PaAssets.builder().assetsoverseas(null).build()).build();
        Assertions.assertThat(OverseasCopiesMapper.mapOverseasCopies(assetsOverseasNull)).isNull();

        PaForm assetsOverseasTrue = PaForm.builder()
                .assets(PaAssets.builder().assetsoverseas(Boolean.TRUE).build()).build();
        Assertions.assertThat(OverseasCopiesMapper.mapOverseasCopies(assetsOverseasTrue)).isNull();

        PaForm assetsNull = PaForm.builder().assets(null).build();
        Assertions.assertThat(OverseasCopiesMapper.mapOverseasCopies(assetsNull)).isNull();
    }
}
