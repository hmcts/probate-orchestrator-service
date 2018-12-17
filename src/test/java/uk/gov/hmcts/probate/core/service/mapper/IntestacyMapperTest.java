package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import static org.assertj.core.api.Assertions.assertThat;

public class IntestacyMapperTest {

    private IntestacyMapper mapper = Mappers.getMapper(IntestacyMapper.class);
    private IntestacyForm intestacyForm;
    private GrantOfRepresentation grantOfRepresentation;

    @BeforeEach
    void setUp() {
        intestacyForm = IntestacyTestDataCreator.createIntestacyForm();
        grantOfRepresentation = IntestacyTestDataCreator.createGrantOfRepresentation();
    }

    @Test
    void shouldMapIntestacyFormToGrantOfRepresentation() {
        GrantOfRepresentation actualGrantOfRepresentation = mapper.toCaseData(intestacyForm);
        assertThat(actualGrantOfRepresentation).isEqualToComparingFieldByFieldRecursively(grantOfRepresentation);
    }

    @Test
    void shouldMapGrantOfRepresentationToGrantOfIntestacyForm() {
        IntestacyForm actualIntestacyForm = mapper.fromCaseData(grantOfRepresentation);
        assertThat(actualIntestacyForm).isEqualToComparingFieldByFieldRecursively(intestacyForm);
    }
}
