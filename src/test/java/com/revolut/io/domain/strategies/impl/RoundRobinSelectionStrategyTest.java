package com.revolut.io.domain.strategies.impl;

import com.revolut.io.domain.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.revolut.io.domain.TestUtils.BACKEND_INSTANCES;
import static com.revolut.io.domain.TestUtils.BACKEND_ROUND_ROBIN_INSTANCES;

public class RoundRobinSelectionStrategyTest {
    private final SelectionStrategy selectionStrategy =
            new RoundRobinSelectionStrategy();

    @Test
    public void selectInstance_ReturnInstanceInRoundRobin() {
        // When & Then
        for(var expectedInstance : BACKEND_ROUND_ROBIN_INSTANCES) {
            Assertions.assertEquals(
                    expectedInstance,
                    selectionStrategy.selectInstance(BACKEND_INSTANCES)
            );
        }
    }

    @ParameterizedTest
    @MethodSource("com.revolut.io.domain.TestUtils#invalidSelectInstanceParams")
    public void selectInstance_WhenInvalidParamProvided_ShouldThrowException(
            List<String> instances, Class<Exception> exceptionClass
    ) {
        // When & Then
        Assertions.assertThrows(exceptionClass, () ->
                selectionStrategy.validateInstances(instances)
        );
    }
}
