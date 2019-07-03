package uk.gov.hmcts.probate.client.persistence;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;

@FeignClient(
        name = "persistence-service-api",
        url = "${persistence.service.api.url}",
        path = "formdata",
        configuration = PersistenceServiceConfiguration.class
)
public interface PersistenceServiceApi {

    @GetMapping(path = "/")
    FormDataResource getFormDatas();

    @GetMapping(path = "/")
    FormDataResource getFormDataWithPageAndSize(@RequestParam("page") String pageNumber, @RequestParam("size")String size);
}
