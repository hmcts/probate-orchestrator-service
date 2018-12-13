package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.forms.Form;

import java.util.Map;

@Component
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    @Autowired
    public SubmitServiceImpl(Map<ProbateType, FormMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public Form saveDrafts(Form form) {
        FormMapper formMapper = mappers.get(form.getType());
        CaseData caseData = formMapper.map(form);
        //Map back from case to form
        return null;
    }
}
