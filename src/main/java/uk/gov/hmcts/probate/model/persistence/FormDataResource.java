package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedResources;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDataResource extends AbstractResource {

    @JsonProperty("_embedded")
    private FormDataContent content;

    @Builder
    public FormDataResource(Links links, PagedResources.PageMetadata pageMetadata, FormDataContent content) {
        super(links, pageMetadata);
        this.content = content;
    }
}
