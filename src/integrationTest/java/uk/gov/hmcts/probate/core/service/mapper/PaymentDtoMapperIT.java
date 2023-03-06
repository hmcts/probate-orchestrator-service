package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PaymentDtoMapperIT {

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(20000);
    private static final String CCD_CASE_NUMBER = "234937229";
    private static final String ONLINE = "online";
    private static final String GBP = "GBP";
    private static final String EXTERNAL_REFERENCE = "EXTERNALREF12345";
    private static final String REFERENCE = "PAYMENT_REF";
    private static final String SITE_ID = "SITEID122344";
    private Date dateCreated = new Date();

    @Autowired
    private PaymentDtoMapper paymentDtoMapper;

    @Test
    public void shouldMapToCasePayment() {
        PaymentDto paymentDto = PaymentDto.builder()
            .reference(REFERENCE)
            .status(PaymentStatus.SUCCESS.getName())
            .amount(AMOUNT)
            .ccdCaseNumber(CCD_CASE_NUMBER)
            .channel(ONLINE)
            .currency(GBP)
            .dateCreated(dateCreated)
            .externalReference(EXTERNAL_REFERENCE)
            .siteId(SITE_ID)
            .build();

        CasePayment casePayment = paymentDtoMapper.toCasePayment(paymentDto);

        assertThat(casePayment.getAmount(), equalTo(AMOUNT.multiply(BigDecimal.valueOf(100L)).longValue()));
        assertThat(casePayment.getStatus(), equalTo(PaymentStatus.SUCCESS));
        assertThat(casePayment.getMethod(), equalTo(ONLINE));
        assertThat(casePayment.getTransactionId(), equalTo(EXTERNAL_REFERENCE));
        assertThat(casePayment.getDate(), equalTo(dateCreated));
        assertThat(casePayment.getReference(), equalTo(REFERENCE));
        assertThat(casePayment.getSiteId(), equalTo(SITE_ID));
    }
}
