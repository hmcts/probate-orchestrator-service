package uk.gov.hmcts.probate.client.validation;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.FieldError;
import uk.gov.hmcts.probate.domain.Applicant;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.ccd.data.AddressDTO;
import uk.gov.hmcts.probate.dto.formdata.ApplicantDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationErrorDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationStatusDTO;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "businessvalidationservice", port = "8889")
@SpringBootTest({
				// overriding provider address
				"businessvalidationservice.ribbon.listOfServers: localhost:8889",
                "business-validation-service.url : localhost:8889"
})
class BusinessValidationServiceConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    @Autowired
	private BusinessValidationClient businessValidationClient;
	private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

	@Pact(state = "provider validates formdata with success", provider = "businessvalidationservice", consumer = "businessvalidationclient")
	RequestResponsePact executeSuccessBusinessValidationPact(PactDslWithProvider builder) {
		// @formatter:off
		FormDataDTO formData = getFormData();
		JSONObject jsonObject = createJsonObject(formData, true);
		return builder
						.given("provider validates formdata with success")
						.uponReceiving("a request to POST a validation")
							.path("/probateTypes/intestacy/validations")
							.method("POST")
							.headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
							.body(jsonObject )
						.willRespondWith()
							.status(200)
							.matchHeader("Content-Type", "application/json;charset=UTF-8")
							.body(createJsonObject(getSuccessResponse(), false))
						.toPact();
		// @formatter:on
	}

    @Pact(state = "provider validates formdata with errors", provider = "businessvalidationservice", consumer = "businessvalidationclient")
    RequestResponsePact executeErrorsBusinessValidationPact(PactDslWithProvider builder) {
        // @formatter:off
        FormDataDTO formData = getFormData();
        JSONObject jsonObject = createJsonObject(formData, true);
        return builder
                    .given("provider validates formdata with errors")
                    .uponReceiving("a request to POST a validation")
                        .path("/probateTypes/intestacy/validations")
                        .method("POST")
                        .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                        .body(jsonObject )
                    .willRespondWith()
                        .status(200)
                        .matchHeader("Content-Type", "application/json;charset=UTF-8")
                        .body(createJsonObject(getErrorResponse(), false))
                    .toPact();
        // @formatter:on
    }


    @Test
    @PactTestFor(pactMethod = "executeSuccessBusinessValidationPact")
    void  verifyExecuteSuccessBusinessValidationPact() {
        FormDataDTO formData = getFormData();

        BusinessValidationResponseDTO businessValidationResponseDTO  =businessValidationClient.valididateIntestacy( SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN ,formData);
        assertThat(businessValidationResponseDTO.getStatus(), equalTo(BusinessValidationStatusDTO.SUCCESS));
        assertThat(businessValidationResponseDTO.getErrors().size(), equalTo(0));
    }

    @Test
    @PactTestFor(pactMethod = "executeErrorsBusinessValidationPact")
    void  verifyExecuteErrorBusinessValidationPact() {
        FormDataDTO formData = getFormData();

        BusinessValidationResponseDTO businessValidationResponseDTO  =businessValidationClient.valididateIntestacy( SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN ,formData);
        assertThat(businessValidationResponseDTO.getStatus(), equalTo(BusinessValidationStatusDTO.FAILURE));
        assertThat(businessValidationResponseDTO.getErrors().size(), equalTo(1));
    }

	private FormDataDTO getFormData() {
		FormDataDTO formData = new FormDataDTO();
		ApplicantDTO applicant = new ApplicantDTO();
		applicant.setFirstName("Ruban");
		applicant.setLastName("Mahendran");
		applicant.setAddress("Some address");

		formData.setApplicant(applicant);
		return formData;
	}


    private BusinessValidationResponseDTO getSuccessResponse() {

        BusinessValidationResponseDTO businessValidationResponseDTO = new BusinessValidationResponseDTO(BusinessValidationStatusDTO.SUCCESS,  new ArrayList<>());
        return businessValidationResponseDTO;
    }

    private BusinessValidationResponseDTO getErrorResponse() {

        BusinessValidationErrorDTO error = new BusinessValidationErrorDTO("applicant.firstName", "fieldMinSize" , "Must be at least {min} characters");
        BusinessValidationResponseDTO businessValidationResponseDTO = new BusinessValidationResponseDTO(BusinessValidationStatusDTO.FAILURE, Arrays.asList(error));
        return businessValidationResponseDTO;
    }



	private JSONObject createJsonObject(Object object, boolean wrapRoot) {
        ObjectMapper objectMapper = new ObjectMapper();

        if(wrapRoot) {
            objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        }
		JSONObject result = null;
		String jsonString = "";
		try {
			 jsonString = objectMapper.writeValueAsString(object);
			 result = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}

}
