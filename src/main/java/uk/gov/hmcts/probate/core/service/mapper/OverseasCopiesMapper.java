package uk.gov.hmcts.probate.core.service.mapper;

import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

@UtilityClass
public class OverseasCopiesMapper {

    public Long mapOverseasCopies(PaForm paForm) {
        if (paForm.getCopies() != null && paForm.getCopies().getOverseas() != null) {
            return paForm.getCopies().getOverseas();
        }

        if (paForm.getAssets() != null && paForm.getAssets().getAssetsoverseas() != null
            && !paForm.getAssets().getAssetsoverseas()) {
            return 0L;
        }
        return null;
    }
}
