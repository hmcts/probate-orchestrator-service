package uk.gov.hmcts.probate.core.service;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FeatureToggleServiceImplTest {
    @Mock
    LDClientInterface ldClientMock;
    @Mock
    LDContext ldContextMock;

    FeatureToggleServiceImpl featureToggleService;

    AutoCloseable closeableMocks;

    @BeforeEach
    void setUp() {
        closeableMocks = MockitoAnnotations.openMocks(this);

        featureToggleService = new FeatureToggleServiceImpl(
                ldClientMock,
                ldContextMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeableMocks.close();
    }

    static Stream<Arguments> lookupFromLDQueriedParams() {
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(true, false),
                Arguments.of(false, false),
                Arguments.of(false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("lookupFromLDQueriedParams")
    void lookupFromLDQueried(
            final boolean expected,
            final boolean defaultValue) {
        final String featureToggleCode = "featureToggleCode";

        when(ldClientMock.boolVariation(
                any(),
                eq(ldContextMock),
                anyBoolean()))
                .thenReturn(expected);

        final boolean actual = featureToggleService.isFeatureToggleOn(featureToggleCode, defaultValue);

        verify(ldClientMock).boolVariation(
                eq(featureToggleCode),
                eq(ldContextMock),
                eq(defaultValue));
        assertThat(actual, equalTo(expected));
    }
}
