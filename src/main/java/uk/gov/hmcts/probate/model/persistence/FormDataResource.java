package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedResources;


@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDataResource {

    @JsonProperty("_embedded")
    private Content content;

    @JsonProperty("_links")
    private Links links;

    @JsonProperty("page")
    private PagedResources.PageMetadata pageMetadata;
}
