package uk.gov.hmcts.probate.dto.ccd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CollectionMemberDTO<T> {

    private final String id;

    private final T value;
}
