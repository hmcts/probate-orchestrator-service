package uk.gov.hmcts.probate.core.service;

import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FeatureToggleServiceImplTest {
    @Mock
    private LDClientInterface ldClientMock;

    private FeatureToggleServiceImpl featureToggleService;

    private AutoCloseable closeableMocks;

    @BeforeEach
    void setup() {
        closeableMocks = MockitoAnnotations.openMocks(this);

        featureToggleService = new FeatureToggleServiceImpl(ldClientMock, "", "", "");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeableMocks.close();
    }

    @Test
    void returnsTrueWhenIronMountainInBackOffice() {
        when(ldClientMock.boolVariation(
                eq(FeatureToggleServiceImpl.IRON_MOUNTAIN_IN_BACK_OFFICE),
                any(),
                anyBoolean()))
                .thenReturn(true);

        final boolean result = featureToggleService.isIronMountainInBackOffice();

        assertTrue(result, "Expected true from client returning true");
        verify(ldClientMock).boolVariation(
                eq(FeatureToggleServiceImpl.IRON_MOUNTAIN_IN_BACK_OFFICE),
                any(),
                anyBoolean());
        verifyNoMoreInteractions(ldClientMock);
    }

    @Test
    void returnsFalseWhenNotIronMountainInBackOffice() {
        when(ldClientMock.boolVariation(
                eq(FeatureToggleServiceImpl.IRON_MOUNTAIN_IN_BACK_OFFICE),
                any(),
                anyBoolean()))
                .thenReturn(false);

        final boolean result = featureToggleService.isIronMountainInBackOffice();

        assertFalse(result, "Expected false from client returning false");
        verify(ldClientMock).boolVariation(
                eq(FeatureToggleServiceImpl.IRON_MOUNTAIN_IN_BACK_OFFICE),
                any(),
                anyBoolean());
        verifyNoMoreInteractions(ldClientMock);
    }

    @Test
    void returnsTrueWhenExelaInBackOffice() {
        when(ldClientMock.boolVariation(eq(FeatureToggleServiceImpl.EXELA_IN_BACK_OFFICE), any(), anyBoolean()))
                .thenReturn(true);

        final boolean result = featureToggleService.isExelaInBackOffice();

        assertTrue(result, "Expected true from client returning true");
        verify(ldClientMock).boolVariation(
                eq(FeatureToggleServiceImpl.EXELA_IN_BACK_OFFICE),
                any(),
                anyBoolean());
        verifyNoMoreInteractions(ldClientMock);
    }

    @Test
    void returnsFalseWhenNotExelaInBackOffice() {
        when(ldClientMock.boolVariation(eq(FeatureToggleServiceImpl.EXELA_IN_BACK_OFFICE), any(), anyBoolean()))
                .thenReturn(false);

        final boolean result = featureToggleService.isExelaInBackOffice();

        assertFalse(result, "Expected false from client returning false");
        verify(ldClientMock).boolVariation(
                eq(FeatureToggleServiceImpl.EXELA_IN_BACK_OFFICE),
                any(),
                anyBoolean());
        verifyNoMoreInteractions(ldClientMock);
    }
}
