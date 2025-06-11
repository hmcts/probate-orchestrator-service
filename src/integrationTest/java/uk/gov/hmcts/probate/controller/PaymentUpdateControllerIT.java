package uk.gov.hmcts.probate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.model.exception.InvalidTokenException;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentUpdateControllerIT {

    public static final String AUTH_TOKEN = "dummyToken";
    public static final String AUTH_TOKEN_EMPTY = " ";
    private static final String PAYMENT_UPDATES = "/payment-updates";

    @MockitoBean
    private SecurityUtils securityUtils;

    private PaymentUpdateController paymentUpdateController;

    @MockitoBean
    private PaymentUpdateService paymentUpdateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldUpdatePayment() throws Exception {
        String paymentDtoJsonStr = TestUtils.getJsonFromFile("paymentDto.json");
        when(securityUtils.checkIfServiceIsAllowed(AUTH_TOKEN)).thenReturn(true);

        mockMvc.perform(put(PAYMENT_UPDATES)
            .header("ServiceAuthorization", AUTH_TOKEN)
            .content(paymentDtoJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(paymentUpdateService, times(1)).paymentUpdate(any(PaymentDto.class));
    }

    @Test
    public void shouldNotUpdatePayment() throws Exception {
        String paymentDtoJsonStr = TestUtils.getJsonFromFile("paymentDto.json");
        when(securityUtils.checkIfServiceIsAllowed(AUTH_TOKEN_EMPTY)).thenReturn(Boolean.FALSE);

        mockMvc.perform(put(PAYMENT_UPDATES)
            .header("ServiceAuthorization", AUTH_TOKEN_EMPTY)
            .content(paymentDtoJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
        verify(paymentUpdateService, times(0)).paymentUpdate(any(PaymentDto.class));
    }

    @Test
    public void shouldNotUpdatePaymentAndInvalidTokenException() throws Exception {
        String paymentDtoJsonStr = TestUtils.getJsonFromFile("paymentDto.json");
        when(securityUtils.checkIfServiceIsAllowed(AUTH_TOKEN_EMPTY))
            .thenThrow(new InvalidTokenException("Provided S2S token is missing or invalid"));

        mockMvc.perform(put(PAYMENT_UPDATES)
            .header("ServiceAuthorization", AUTH_TOKEN_EMPTY)
            .content(paymentDtoJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());

        verify(paymentUpdateService, times(0)).paymentUpdate(any(PaymentDto.class));

    }
}
