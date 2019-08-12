package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedResources;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteDataResource extends AbstractResource {

    @JsonProperty("_embedded")
    private InviteDataContent content;

    @Builder
    public InviteDataResource(Links links, PagedResources.PageMetadata pageMetadata, InviteDataContent content) {
        super(links, pageMetadata);
        this.content = content;
    }
}
