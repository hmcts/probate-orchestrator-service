package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

public final class OverseasCopiesMapper {

    private OverseasCopiesMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Long mapOverseasCopies(PaForm paForm) {
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
