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
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntestacyMapperTest {

    @Autowired
    private IntestacyMapper mapper;

    private IntestacyForm intestacyForm;
    private GrantOfRepresentationData grantOfRepresentation;

    @Before
    public void setUp() {
        intestacyForm = IntestacyTestDataCreator.createIntestacyForm();
        grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
    }

//    @Test
//    public void shouldMapIntestacyFormToGrantOfRepresentation() {
//        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(intestacyForm);
//        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(grantOfRepresentation);
//    }
//
//    @Test
//    public void shouldMapGrantOfRepresentationToGrantOfIntestacyForm() {
//        IntestacyForm actualIntestacyForm = mapper.fromCaseData(grantOfRepresentation);
//        actualIntestacyForm.setDeclaration(null);
//        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(intestacyForm);
//    }

    @Test
    public void shouldMapNullIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(null);
        Assert.assertThat(actualGrantOfRepresentation, is(nullValue()));
    }

    @Test
    public void shouldMapNullGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(null);
        Assert.assertThat(actualIntestacyForm, is(nullValue()));
    }

    @Test
    public void shouldMapEmptyIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentationData expectedGrantOfRepresentation = new GrantOfRepresentationData();
        expectedGrantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        expectedGrantOfRepresentation.setGrantType(GrantType.INTESTACY);
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(new IntestacyForm());
        Assert.assertThat(actualGrantOfRepresentation, equalTo(expectedGrantOfRepresentation));
        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(expectedGrantOfRepresentation);

    }

    @Test
    public void shouldMapEmptyGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm expectedIntestacyForm = new IntestacyForm();
        expectedIntestacyForm.setType(ProbateType.INTESTACY);
        expectedIntestacyForm.setCaseType(GrantType.INTESTACY.getName());
        expectedIntestacyForm.setCopies(new Copies());
        expectedIntestacyForm.setIht(new InheritanceTax());
        expectedIntestacyForm.setRegistry(new Registry());
        expectedIntestacyForm.setApplicant(new IntestacyApplicant());
        expectedIntestacyForm.setDeceased(new IntestacyDeceased());
        expectedIntestacyForm.setDeclaration(new Declaration());
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(new GrantOfRepresentationData());
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(expectedIntestacyForm);
    }
}
