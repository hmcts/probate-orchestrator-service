package uk.gov.hmcts.probate.core.service.migration;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class IdamUsersCsvLoader {

    public List<IdamUserEmail> loadIdamUserList(String fileName) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.typedSchemaFor(IdamUserEmail.class);
            List list = new CsvMapper().readerFor(IdamUserEmail.class)
                    .with(csvSchema.withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR))
                    .readValues(IdamUsersCsvLoader.class.getClassLoader().getResource(fileName))
                    .readAll();

            return list;
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

}
