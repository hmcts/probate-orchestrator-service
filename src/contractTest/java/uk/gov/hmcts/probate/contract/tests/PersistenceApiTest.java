package uk.gov.hmcts.probate.contract.tests;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest({
        // overriding provider address
        "persistence.service.api.url : probate-persistence-service-aat.service.core-compute-aat.internal",
        "submit.service.api.url : probate-submit-service-aat.service.core-compute-aat.internal"
})
public class PersistenceApiTest {

    @Autowired
    private FormDataMigrationService formDataMigrationService;

    @Autowired
    private PersistenceServiceApi persistenceApi;

    @Before
    public void setUpTest(){
        System.getProperties().setProperty("http.proxyHost", "proxyout.reform.hmcts.net");
        System.getProperties().setProperty("http.proxyPort", "8080");
    }

    @Test
    public void testMigration(){
        formDataMigrationService.migrateFormData();

    }


    @Test
    public void testGetAll(){
        FormDataResource results =  persistenceApi.getFormDataWithPageAndSize("559","20");
        results.getContent();
        Assert.assertThat(results.getPageMetadata().getNumber(), equalTo(3) );
    }
}
