package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ExecutorNamesMapperTest {

    private Executor executor;

    private static Stream<Arguments> executorName() {
        return Stream.of(arguments(null, "Smith"), arguments("James", null));
    }

    private static Stream<String> executor() {
        return Stream.of(null, "");
    }

    @ParameterizedTest
    @MethodSource("executorName")
    void shouldReturnNullWhenEitherNameIsNull(final String firstName, final String lastName) {
        executor = Executor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        assertThat(ExecutorNamesMapper.getFullname(executor), IsNull.nullValue());
    }

    @ParameterizedTest
    @MethodSource("executor")
    void shouldReturnNullWhenNameIsNullOrEmpty(final String fullName) {
        executor = Executor.builder()
                .fullName(fullName)
                .build();
        assertThat(ExecutorNamesMapper.getFullname(executor), IsNull.nullValue());
    }

    @Test
    void shouldReturnName() {
        executor = Executor.builder()
                .firstName("James")
                .lastName("Smith")
                .build();
        assertEquals("James Smith", ExecutorNamesMapper.getFullname(executor));
    }

    @Test
    void shouldReturnNullWhenExecutorIsNull() {
        assertThat(ExecutorNamesMapper.getFullname(null), IsNull.nullValue());
    }
}
