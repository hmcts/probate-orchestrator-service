package uk.gov.hmcts.probate.core.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.DeclarationDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.Equality;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Language;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Will;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaApplicant;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PaMapperIT {

    @Autowired
    private PaMapper mapper;

    private PaForm paFormMultipleExecutors;
    private GrantOfRepresentationData grantOfRepresentationMultipleExecutors;

    private PaForm paFormSingleExecutor;
    private GrantOfRepresentationData grantOfRepresentationSingleExecutor;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static String APPLICANT_ADDRESSES = "[{\"formatted_address\":\"102 Petty France London SW1H 9EX\"}]";

    @BeforeEach
    public void setUp() throws IOException {
        paFormMultipleExecutors = PaMultipleExecutorTestDataCreator.createPaForm();
        grantOfRepresentationMultipleExecutors = PaMultipleExecutorTestDataCreator.createGrantOfRepresentation();

        paFormSingleExecutor = PaSingleExecutorTestDataCreator.createPaForm();
        grantOfRepresentationSingleExecutor = PaSingleExecutorTestDataCreator.createGrantOfRepresentation();
    }

    @Test
    public void shouldMapPaFormToGrantOfRepresentationMultipleExecutors() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(paFormMultipleExecutors);
        assertThat(actualGrantOfRepresentation)
            .isEqualToComparingFieldByFieldRecursively(grantOfRepresentationMultipleExecutors);
    }

    @Test
    public void shouldMapGrantOfRepresentationToPaFormMultipleExecutors() {
        PaForm actualPaForm = mapper.fromCaseData(grantOfRepresentationMultipleExecutors);
        assertThat(actualPaForm).isEqualToComparingFieldByFieldRecursively(paFormMultipleExecutors);
        assertThat(actualPaForm.getExecutors().getList()).isNotEmpty();
    }

    @Test
    public void shouldMapNullPaFormToGrantOfRepresentation() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(null);
        assertNull(actualGrantOfRepresentation);
    }

    @Test
    public void shouldNotMapGrantOfRepresentationToPaFormAliasReason() {
        paFormMultipleExecutors.setApplicant(PaApplicant.builder()
            .alias("King of the North")
            .aliasReason("")
            .address(Address.builder().addressLine1("The Wall")
                .postTown("North Westeros").postCode("GOT567").formattedAddress("The Wall North Westeros GOT567")
                .build())
            .lastName("Smith")
            .firstName("John")
            .phoneNumber("00000000")
            .postcode("HA5")
            .nameAsOnTheWill(true)
            .build());
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(paFormMultipleExecutors);
        assertThat(actualGrantOfRepresentation.getPrimaryApplicantAliasReason()).isNull();
    }

    @Test
    public void shouldMapNullGrantOfRepresentationToGrantOfPaForm() {
        PaForm actualPaForm = mapper.fromCaseData(null);
        assertNull(actualPaForm);
    }

    @Test
    public void shouldMapEmptyPaFormToGrantOfRepresentation() {
        GrantOfRepresentationData expectedGrantOfRepresentation = new GrantOfRepresentationData();
        expectedGrantOfRepresentation.setApplicationType(ApplicationType.PERSONAL);
        expectedGrantOfRepresentation.setGrantType(GrantType.GRANT_OF_PROBATE);
        expectedGrantOfRepresentation.setApplicationSubmittedDate(LocalDate.now());
        expectedGrantOfRepresentation.setNumberOfApplicants(0L);
        expectedGrantOfRepresentation.setBoDocumentsUploaded(Lists.newArrayList());
        expectedGrantOfRepresentation.setPrimaryApplicantIsApplying(true);
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(new PaForm());
        assertThat(actualGrantOfRepresentation)
            .isEqualToComparingFieldByFieldRecursively(expectedGrantOfRepresentation);

    }

    @Test
    public void shouldMapEmptyGrantOfRepresentationToGrantOfPaForm() {
        PaForm expectedPaForm = new PaForm();
        expectedPaForm.setType(ProbateType.PA);
        expectedPaForm.setCaseType(GrantType.GRANT_OF_PROBATE.getName());
        expectedPaForm.setCopies(new Copies());
        expectedPaForm.setLanguage(new Language());
        PaAssets paAssets = new PaAssets();
        paAssets.setAssetsoverseas(null);
        expectedPaForm.setAssets(paAssets);
        expectedPaForm.setIht(new InheritanceTax());
        expectedPaForm.setRegistry(new Registry());
        expectedPaForm.setApplicant(new PaApplicant());
        expectedPaForm.setDeceased(new PaDeceased());
        expectedPaForm.setWill(new Will());
        expectedPaForm.setExecutors(new Executors());
        Declaration declaration = new Declaration();
        declaration.setDeclaration(DeclarationDeclaration.builder().build());
        declaration.setLegalStatement(LegalStatement.builder().build());
        expectedPaForm.setDeclaration(declaration);
        expectedPaForm.setEquality(new Equality());
        PaForm actualPaForm = mapper.fromCaseData(new GrantOfRepresentationData());
        assertThat(actualPaForm).isEqualToComparingFieldByFieldRecursively(expectedPaForm);
    }

    @Test
    public void shouldMapPaFormToGrantOfRepresentationSingleExecutor() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(paFormSingleExecutor);
        assertThat(actualGrantOfRepresentation)
            .isEqualToComparingFieldByFieldRecursively(grantOfRepresentationSingleExecutor);
    }

    @Test
    public void shouldMapGrantOfRepresentationToPaFormSingleExecutor() {
        PaForm actualPaForm = mapper.fromCaseData(grantOfRepresentationSingleExecutor);
        assertThat(actualPaForm).isEqualToComparingFieldByFieldRecursively(paFormSingleExecutor);
        assertThat(actualPaForm.getExecutors().getList()).isNotEmpty();
    }
}
