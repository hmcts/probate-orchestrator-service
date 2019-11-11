package uk.gov.hmcts.probate.core.service.migration;

import org.hamcrest.core.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class IdamUsersCsvLoaderTest {

    private IdamUsersCsvLoader csvLoader = new IdamUsersCsvLoader();

    @Test
    public void shouldLoadCsvFile() {
        List<IdamUserEmail> idamUserEmails = csvLoader.loadIdamUserList("idam_ids.csv");
        Assert.assertThat(idamUserEmails.size(), Is.is(5));
    }
}
