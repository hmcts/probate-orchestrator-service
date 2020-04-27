package uk.gov.hmcts.probate.core.service.mapper;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.forms.Equality;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatApplicant;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatDeceased;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaveatMapperTest {

    @Autowired
    private CaveatMapper mapper;

    private CaveatForm caveatForm;
    private CaveatData caveatData;

    @Before
    public void setUp() {
        caveatForm = CaveatTestDataCreator.createCaveatForm();
        caveatData = CaveatTestDataCreator.createCaveatData();
    }

    @Test
    public void shouldMapCaveatFormToCaveatData() {
        CaveatData caveatData = mapper.toCaseData(caveatForm);
        assertThat(caveatData).isEqualToComparingFieldByFieldRecursively(this.caveatData);
    }

    @Test
    public void shouldMapCaveatDataToCaveatForm() {
        CaveatForm actualCaveatForm = mapper.fromCaseData(caveatData);
        assertThat(actualCaveatForm).isEqualToComparingFieldByFieldRecursively(caveatForm);
    }

    @Test
    public void shouldMapNullCaveatFormToCaveatData() {
        CaveatData actualCaveatData = mapper.toCaseData(null);
        Assert.assertThat(actualCaveatData, is(nullValue()));
    }

    @Test
    public void shouldMapNullCaveatDataToCaveatForm() {
        CaveatForm actualCaveatForm = mapper.fromCaseData(null);
        Assert.assertThat(actualCaveatForm, is(nullValue()));
    }

    @Test
    public void shouldMapEmptyCaveatFormToCaveatData() {
        CaveatData expectedCaveatData = new CaveatData();
        expectedCaveatData.setApplicationType(ApplicationType.PERSONAL);
        expectedCaveatData.setCaveatRaisedEmailNotificationRequested(true);
        expectedCaveatData.setPaperForm(false);
        CaveatData actualCaveatData = mapper.toCaseData(new CaveatForm());
        Assert.assertThat(actualCaveatData, equalTo(expectedCaveatData));
        assertThat(actualCaveatData).isEqualToComparingFieldByFieldRecursively(expectedCaveatData);
    }

    @Test
    public void shouldMapEmptyCaveatDataToCaveatForm() {
        CaveatForm caveatForm = new CaveatForm();
        caveatForm.setType(ProbateType.CAVEAT);
        caveatForm.setRegistry(new Registry());
        caveatForm.setApplicant(new CaveatApplicant());
        caveatForm.setDeceased(new CaveatDeceased());
        caveatForm.setLanguage(new Language());
        caveatForm.setEquality(new Equality());
        CaveatForm actualCaveatForm = mapper.fromCaseData(new CaveatData());
        assertThat(actualCaveatForm).isEqualToComparingFieldByFieldRecursively(caveatForm);
    }
}
