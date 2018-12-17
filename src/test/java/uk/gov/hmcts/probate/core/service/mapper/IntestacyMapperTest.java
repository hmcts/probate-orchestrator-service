package uk.gov.hmcts.probate.core.service.mapper;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyAssets;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class IntestacyMapperTest {

    private IntestacyMapper mapper = Mappers.getMapper(IntestacyMapper.class);
    private IntestacyForm intestacyForm;
    private GrantOfRepresentation grantOfRepresentation;

    @Before
    public void setUp() {
        intestacyForm = IntestacyTestDataCreator.createIntestacyForm();
        grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
    }

    @Test
    public void shouldMapIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentation actualGrantOfRepresentation = mapper.toCaseData(intestacyForm);
        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(grantOfRepresentation);
    }

    @Test
    public void shouldMapGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(grantOfRepresentation);
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(intestacyForm);
    }

    @Test
    public void shouldMapNullIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentation actualGrantOfRepresentation = mapper.toCaseData(null);
        Assert.assertThat(actualGrantOfRepresentation, is(nullValue()));
    }

    @Test
    public void shouldMapNullGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(null);
        Assert.assertThat(actualIntestacyForm, is(nullValue()));
    }

    @Test
    public void shouldMapEmptyIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentation expectedGrantOfRepresentation = new GrantOfRepresentation();
        GrantOfRepresentation actualGrantOfRepresentation = mapper.toCaseData(new IntestacyForm());
        Assert.assertThat(actualGrantOfRepresentation, equalTo(expectedGrantOfRepresentation));
        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(expectedGrantOfRepresentation);

    }

    @Test
    public void shouldMapEmptyGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm expectedIntestacyForm = new IntestacyForm();
        expectedIntestacyForm.setCopies(new Copies());
        expectedIntestacyForm.setAssets(new IntestacyAssets());
        expectedIntestacyForm.setIht(new InheritanceTax());
        expectedIntestacyForm.setRegistry(new Registry());
        expectedIntestacyForm.setApplicant(new IntestacyApplicant());
        expectedIntestacyForm.setDeceased(new IntestacyDeceased());
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(new GrantOfRepresentation());
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(expectedIntestacyForm);
    }
}
