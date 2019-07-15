package uk.gov.hmcts.probate.client.persistence;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.model.persistence.InviteDataResource;

import java.time.LocalDate;

@FeignClient(
        name = "persistence-service-api",
        url = "${persistence.service.api.url}",
        configuration = PersistenceServiceConfiguration.class
)
public interface PersistenceServiceApi {

    @GetMapping(path = "/formdata")
    FormDataResource getFormDatas();

    @GetMapping(path = "/formdata")
    FormDataResource getFormDataWithPageAndSize(@RequestParam("page") String pageNumber, @RequestParam("size") String size);

    @GetMapping(path = "/formdata/search/findByCreatedAfterDate")
    FormDataResource getFormDataByAfterCreateDate(@RequestParam("startDate") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate);

    @GetMapping(path = "/formdata/search/findByCreatedAfterDate")
    FormDataResource getPagedFormDataByAfterCreateDate(@RequestParam("startDate") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,@RequestParam("page") String pageNumber, @RequestParam("size") String size);

    @GetMapping(path = "/invitedata")
    InviteDataResource getInviteDatas();

    @GetMapping(path = "/invitedata")
    InviteDataResource  getInviteDataWithPageAndSize(@RequestParam("page") String pageNumber, @RequestParam("size") String size);

}
