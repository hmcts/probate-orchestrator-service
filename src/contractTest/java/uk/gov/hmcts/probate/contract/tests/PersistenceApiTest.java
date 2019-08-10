//package uk.gov.hmcts.probate.contract.tests;
//
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
//import uk.gov.hmcts.probate.core.service.SecurityUtils;
//import uk.gov.hmcts.probate.core.service.migration.FormDataMigrator;
//import uk.gov.hmcts.probate.model.persistence.FormDataResource;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest({
//        "persistence.service.api.url : probate-persistence-service-aat.service.core-compute-aat.internal"
//})
//public class PersistenceApiTest {
//
//    @Autowired
//    private FormDataMigrator formDataMigrationService;
//
//    @Autowired
//    private PersistenceServiceApi persistenceApi;
//
//    @Autowired
//    private SecurityUtils securityUtils;
//
//    @Before
//    public void setUpTest() {
//        System.getProperties().setProperty("http.proxyHost", "proxyout.reform.hmcts.net");
//        System.getProperties().setProperty("http.proxyPort", "8080");
//        securityUtils.setSecurityContextUserAsCaseworker();
//    }
//
//    @Test
//    public void testMigration(){
//
//        formDataMigrationService.migrateFormData();
//
//    }
//
//
//    @Test
//    public void testGetPage(){
//        FormDataResource results =  persistenceApi.getFormDataWithPageAndSize("189","20");
//        results.getContent();
//        Assert.assertThat(results.getPageMetadata().getNumber(), equalTo(3) );
//    }
//}
