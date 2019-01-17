package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8889", "spring.application.name=PACT_TEST"
})
@PactBroker(host = "${pact.broker.baseUrl}", port = "${pact.broker.port}")
@Provider("probate_orchestrator_intestacyformdataperistence_provider")
public class FormsControllerDraftProviderTest {

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1930, 01, 01);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 01, 01);

    @MockBean
    private SubmitService submitService;

    @TestTarget
    @SuppressWarnings(value = "VisibilityModifier")
    public final Target target = new HttpTarget("http", "localhost", 8889, "/");

    @Before
    public void setUpTest() {
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
    }

    @State({"provider persists intestacy formdata with success",
            "provider persists intestacy formdata with success"})
    public void toPersistIntestacyFormDataWithSuccess() throws IOException, JSONException {

        IntestacyForm form = IntestacyForm.builder()
                .type(ProbateType.INTESTACY)
                .applicant(
                        IntestacyApplicant.builder()
                                .address("Pret a Manger St. Georges Hospital Blackshaw Road London SW17 0QT")
                                .addressFound(Boolean.TRUE)
                                .adoptionInEnglandOrWales(Boolean.TRUE)
                                .email("someemailaddress@host.com")
                                .firstName("Jon")
                                .freeTextAddress("Pret a Manger St. Georges Hospital Blackshaw Road")
                                .lastName("Snow")
                                .phoneNumber("123455678")
                                .postCode("SW17 0QT")
                                .build())
                .deceased(IntestacyDeceased.builder()
                        .anyDeceasedChildrenDieBeforeDeceased(Boolean.FALSE)
                        .address("Winterfell, Westeros")
                        .addressFound(Boolean.TRUE)
                        .alias(Boolean.TRUE)
                        .allDeceasedChildrenOverEighteen(Boolean.TRUE)
                        .anyChildren(Boolean.FALSE)
                        .dateOfBirth(DATE_OF_BIRTH)
                        .dateOfDeath(DATE_OF_DEATH)
                        .divorcedInEnglandOrWales(Boolean.FALSE)
                        .domiciledInEnglandOrWales(Boolean.TRUE)
                        .firstName("Ned")
                        .freeTextAddress("Winterfell, Westeros")
                        .postCode("SW17 0QT")
                        .lastName("Stark")
                        .anyDeceasedGrandchildrenUnderEighteen(Boolean.FALSE)
                        .build())
                .declaration(IntestacyDeclaration.builder()
                        .declarationAgreement(Boolean.TRUE)
                        .build())
                .build();



        when(submitService.saveDraft(anyString(), any(IntestacyForm.class)))
                .thenReturn(form);
    }

}
