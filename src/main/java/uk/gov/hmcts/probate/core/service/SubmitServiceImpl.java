package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.MapperUtils;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;

import java.util.Map;

@Component
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    private final SubmitServiceApi submitServiceApi;

    @Autowired
    public SubmitServiceImpl(Map<ProbateType, FormMapper> mappers, SubmitServiceApi submitServiceApi) {
        this.mappers = mappers;
        this.submitServiceApi = submitServiceApi;
    }

    @Override
    public Form getCase(String applicantEmail, ProbateType probateType) {
        FormMapper formMapper = mappers.get(probateType);
        return null;
    }

    @Override
    public Form saveDraft(String applicantEmail, Form form) {
        return null;
    }

    @Override
    public Form submit(String applicantEmail, Form form) {
        return null;
    }

    @Override
    public Form updatePayments(String applicantEmail, Form form) {
        return null;
    }
}
