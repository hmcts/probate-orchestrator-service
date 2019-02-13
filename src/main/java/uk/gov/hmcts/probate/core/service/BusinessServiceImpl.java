package uk.gov.hmcts.probate.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

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
        return businessServiceApi.generateCheckAnswersSummaryPdf(
                authorisation,
                serviceAuthorisation,
                checkAnswersSummary
        );
    }

    @Override
    public byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration){
        log.info("generateLegalDeclarationPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return businessServiceApi.generateLegalDeclarationPDF(
                authorisation,
                serviceAuthorisation,
                legalDeclaration
        );
    }


    @Override
    public byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet){
        log.info("generateBulkScanCoverSheetPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return businessServiceApi.generateBulkScanCoverSheetPDF(
                authorisation,
                serviceAuthorisation,
                bulkScanCoverSheet
        );
    }
}
