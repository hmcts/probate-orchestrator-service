package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class IdentifierConfiguration {

    @Bean
    public Map<ProbateType, Function<Form, String>> formIdentifierFunctionMap() {
        return ImmutableMap.<ProbateType, Function<Form, String>>builder()
            .put(ProbateType.INTESTACY, intestacyFormIdentifierFunction())
            .put(ProbateType.CAVEAT, caveatFormIdentifierFunction())
            .put(ProbateType.PA, paFormIdentifierFunction())
            .build();
    }

    private Function<Form, String> intestacyFormIdentifierFunction() {
        return form -> {
            IntestacyForm intestacyForm = (IntestacyForm) form;
            return intestacyForm.getApplicant().getEmail();
        };
    }

    private Function<Form, String> caveatFormIdentifierFunction() {
        return form -> {
            CaveatForm caveatForm = (CaveatForm) form;
            return caveatForm.getApplicationId();
        };
    }

    private Function<Form, String> paFormIdentifierFunction() {
        return form -> {
            PaForm paForm = (PaForm) form;
            return paForm.getApplicantEmail();
        };
    }
}
