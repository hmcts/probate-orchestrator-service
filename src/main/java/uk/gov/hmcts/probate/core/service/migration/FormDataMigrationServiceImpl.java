package uk.gov.hmcts.probate.core.service.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.model.persistence.FormHolder;
import uk.gov.hmcts.probate.model.persistence.LegacyPaForm;
import uk.gov.hmcts.reform.probate.model.ProbateType;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Component
public class FormDataMigrationServiceImpl implements uk.gov.hmcts.probate.service.FormDataMigrationService {

    private final PersistenceServiceApi persistenceServiceApi;
    private final SubmitServiceApi  submitServiceApi;
    private final SecurityUtils securityUtils;

    public void migrateFormData(){

        FormDataResource formDatas = persistenceServiceApi.getFormDatas();
        long totalPages = formDatas.getPageMetadata().getTotalPages();
        long size = formDatas.getPageMetadata().getSize();
        for (int i = 0; i < 50; i++) {
            FormDataResource formDataSet = persistenceServiceApi.getFormDataWithPageAndSize(Integer.toString(i),
                    Long.toString(size));
            Collection<FormHolder> formHolders = formDataSet.getContent().getFormdata();
            formHolders.forEach(f-> {
                LegacyPaForm formdata = f.getFormdata();
                submitServiceApi.getCase(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(), formdata.getApplicantEmail(), ProbateType.PA.getCaseType().name());

            });

        }


    }

}
