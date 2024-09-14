package com.revolut.io.domain.strategies.impl;

import com.revolut.io.domain.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.revolut.io.domain.TestUtils.BACKEND_INSTANCES;

public class RandomSelectionStrategyTest {
    private SelectionStrategy selectionStrategy;

    @BeforeEach
    public void beforeEach() {
        this.selectionStrategy = new RandomSelectionStrategy();
    }

    @Test
    public void selectInstance_ReturnRandomInstanceFromInstanceList() {
        // Given
        var instance = this.selectionStrategy.selectInstance(BACKEND_INSTANCES);
        Assertions.assertTrue(BACKEND_INSTANCES.contains(instance));
    }

    @ParameterizedTest
    @MethodSource("com.revolut.io.domain.TestUtils#invalidSelectInstanceParams")
    public void selectInstance_WhenInvalidParamsAreProvided_ShouldThrowException(
            List<String> list, Class<Exception> exceptionClass
    ) {
        Assertions.assertThrows(exceptionClass, () ->
                this.selectionStrategy.selectInstance(list));
    }
}
