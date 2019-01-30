package uk.gov.hmcts.probate.core.service;

import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

import java.io.IOException;

@Slf4j
@Component
public class BusinessServiceImpl implements BusinessService {

    private final BusinessServiceApi businessServiceApi;
    private final SecurityUtils securityUtils;

    @Autowired
    public BusinessServiceImpl(BusinessServiceApi businessServiceApi,SecurityUtils securityUtils){
        this.businessServiceApi = businessServiceApi;
        this.securityUtils = securityUtils;
    }

    @Override
    public byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary){
        log.info("generateCheckAnswersSummaryPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        feign.Response pdf = businessServiceApi.generateCheckAnswersSummaryPdf(
                authorisation,
                serviceAuthorisation,
                checkAnswersSummary
        );

        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(pdf.body().asInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

}
