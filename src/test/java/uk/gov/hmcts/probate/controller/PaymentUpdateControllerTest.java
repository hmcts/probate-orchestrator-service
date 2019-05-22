package uk.gov.hmcts.probate.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {PaymentUpdateController.class}, secure = false)
public class PaymentUpdateControllerTest {

    private static final String PAYMENT_UPDATES = "/payment-updates";

    @MockBean
    private PaymentUpdateService paymentUpdateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUpdatePayment() throws Exception {
        String paymentDtoJsonStr = TestUtils.getJSONFromFile("paymentDto.json");

        mockMvc.perform(put(PAYMENT_UPDATES)
            .content(paymentDtoJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(paymentUpdateService, times(1)).paymentUpdate(any(PaymentDto.class));
    }
}
