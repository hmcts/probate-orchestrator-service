package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.FormDataDTOTestBuilder;
import uk.gov.hmcts.probate.client.validation.BusinessValidationClient;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;
import uk.gov.hmcts.probate.service.ValidationService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceImplTest {

    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private BusinessValidationClient businessValidationClient;

    private ValidationService  validationServiceImpl;
    public static final String SOME_SECURITY_TOKEN = "someSecurityToken";
    public static final String SOME_USER_TOKEN = "someUserToken";

    @Before
    public void setUpTest(){
        validationServiceImpl = new ValidationServiceImpl(securityUtils, businessValidationClient);
    }

    @Test
    public void shouldValidateIntestacy(){
        FormDataDTO formDataDTO = FormDataDTOTestBuilder.build();
        when(securityUtils.generateServiceToken()).thenReturn(SOME_SECURITY_TOKEN);
        when(securityUtils.getUserToken()).thenReturn(SOME_USER_TOKEN);
        BusinessValidationResponseDTO businessValidationResponseDTO = new BusinessValidationResponseDTO();
        when(businessValidationClient.valididateIntestacy(SOME_USER_TOKEN, SOME_SECURITY_TOKEN, formDataDTO)).thenReturn(businessValidationResponseDTO);
        BusinessValidationResponseDTO result = validationServiceImpl.validate(formDataDTO, ValidationService.ProbateType.INTESTACY);
        verify(businessValidationClient).valididateIntestacy(SOME_USER_TOKEN, SOME_SECURITY_TOKEN, formDataDTO);
    }
}