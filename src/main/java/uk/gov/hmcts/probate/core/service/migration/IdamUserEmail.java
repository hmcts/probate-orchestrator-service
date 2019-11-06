package uk.gov.hmcts.probate.core.service.migration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder(value = {"emailAddress", "idamId"})
@JsonRootName("idamUserEmail")
public class IdamUserEmail {
    @JsonProperty
    private String emailAddress;
    @JsonProperty
    private String idamId;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getIdamId() {
        return idamId;
    }

    public void setIdamId(String idamId) {
        this.idamId = idamId;
    }
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IdamUserEmail{");
        sb.append("emailAddress='").append(emailAddress).append('\'');
        sb.append(", idamId=").append(idamId);
        sb.append('}');
        return sb.toString();
    }
}
