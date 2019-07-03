package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Copies;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Registry;
import uk.gov.hmcts.reform.probate.model.forms.Will;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaAssets;
import uk.gov.hmcts.reform.probate.model.forms.pa.Summary;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegacyPaForm {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private Declaration declaration;

    @NotNull
    private String applicantEmail;

    private LegacyExecutors executors;

    private String uploadDocumentUrl;

    private PaAssets assets;

    private LegacyInheritanceTax iht;

    private Will will;

    private Summary summary;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDate applicationSubmittedDate;

    //submissionReference may need to be removed later on.
    private Long submissionReference;

    private Map<String, Object> legalDeclaration;

    private Map<String, Object> checkAnswersSummary;

    private LegacyProbateType caseType;


    private LegacyDeceased deceased;

    private LegacyApplicant applicant;

    private Registry registry;

    private CcdCase ccdCase;

    private List<Payment> payments;

    private Fees fees;

    private Copies copies;

    private Payment payment;

    @Builder
    public LegacyPaForm(LegacyProbateType type, String applicantEmail, LegacyDeceased deceased, LegacyApplicant applicant,
                        Declaration declaration, String uploadDocumentUrl, Registry registry,
                        CcdCase ccdCase, List<Payment> payments, Copies copies, PaAssets assets,
                        LegacyInheritanceTax iht, Will will, Summary summary, LegacyExecutors executors,
                        LocalDate applicationSubmittedDate, Long submissionReference,
                        Map<String, Object> legalDeclaration, Map<String, Object> checkAnswersSummary, Payment payment,
                        Fees fees) {
        this.applicantEmail = applicantEmail;
        this.declaration = declaration;
        this.uploadDocumentUrl = uploadDocumentUrl;
        this.assets = assets;
        this.iht = iht;
        this.will = will;
        this.summary = summary;
        this.executors = executors;
        this.applicationSubmittedDate = applicationSubmittedDate;
        this.submissionReference = submissionReference;
        this.legalDeclaration = legalDeclaration;
        this.checkAnswersSummary = checkAnswersSummary;
        this.deceased = deceased;
        this.applicant =applicant;
    }
}
