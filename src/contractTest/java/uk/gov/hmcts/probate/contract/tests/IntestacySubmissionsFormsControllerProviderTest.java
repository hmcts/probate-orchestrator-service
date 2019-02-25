package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyAssets;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeclaration;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8890", "spring.application.name=PACT_TEST"
})
@Provider("probate_orchestrator_service_intestacy_submit")
public class IntestacySubmissionsFormsControllerProviderTest extends ControllerProviderTest{

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1930, 01, 01);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 01, 01);

    @MockBean
    private SubmitService submitService;

    @TestTarget
    @SuppressWarnings(value = "VisibilityModifier")
    public final Target target = new HttpTarget("http", "localhost", 8890, "/");

    @State({"probate_orchestrator_service submits intestacy formdata with success",
            "probate_orchestrator_service submits intestacy formdata with success"})
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
                                .relationshipToDeceased(Relationship.ADOPTED_CHILD)
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
                        .otherChildren(Boolean.TRUE)
                        .alias(Boolean.TRUE)
                        .otherNames(getStringAliasOtherNamesMap())
                        .maritalStatus(MaritalStatus.MARRIED)
                        .spouseNotApplyingReason(SpouseNotApplyingReason.MENTALLY_INCAPABLE)
                        .anyChildren(Boolean.FALSE)
                        .allDeceasedChildrenOverEighteen(Boolean.TRUE)
                        .anyDeceasedChildrenDieBeforeDeceased(Boolean.FALSE)
                        .anyDeceasedGrandchildrenUnderEighteen(Boolean.FALSE)
                        .build())
                .iht(InheritanceTax.builder()
                        .form(IhtFormType.IHT205)
                        .method(IhtMethod.ONLINE)
                        .netValue(new BigDecimal(100000))
                        .grossValue(new BigDecimal(100000))
                        .identifier("GOT123456")
                        .build())
                .assets(IntestacyAssets.builder()
                        .assetsOverseas(Boolean.TRUE)
                        .assetsOverseasNetValue(new BigDecimal(100.5))
                        .build())
                .copies(Copies.builder()
                        .overseas(6L)
                        .uk(5L)
                        .build())
                .ccdCase(CcdCase.builder()
                        .id(1535574519543819L)
                        .state("CaseCreated")
                        .build())
                .registry(Registry.builder()
                        .address("Line 1 Bham\nLine 2 Bham\nLine 3 Bham\nPostCode Bham")
                        .name("Birmingham")
                        .email("birmingham@email.com")
                        .sequenceNumber(20075L)
                        .build())
                .declaration(IntestacyDeclaration.builder()
                        .declarationAgreement(Boolean.TRUE)
                        .build())
                .payments(Arrays.asList(Payment.builder()
                        .amount(new BigDecimal(220.5))
                        .siteId("P223")
                        .status(PaymentStatus.SUCCESS)
                        .method("online")
                        .reference("RC-1543-8527-2465-2900")
                        .transactionId("v5bf26kn5rq9rvdq7gsvn7v11d")
                        .date(getPaymentDate())
                        .build()))
                .uploadDocumentUrl("http://document-management/document/12345")
                .build();

        when(submitService.submit(anyString(), any(IntestacyForm.class)))
                .thenReturn(form);
    }

    @NotNull
    private Map<String, AliasOtherNames> getStringAliasOtherNamesMap() {
        return Stream.of(new Object[][] {
                    { "name_0", new AliasOtherNames("King", "North") },
            }).collect(Collectors.toMap(data -> (String) data[0], data -> (AliasOtherNames) data[1]));
    }

    Date getPaymentDate(){
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2018, 12,3);
        return cal.getTime();
    }


}
