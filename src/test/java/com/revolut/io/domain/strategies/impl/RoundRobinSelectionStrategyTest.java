package com.revolut.io.strategies.impl;

import com.revolut.io.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.revolut.io.TestUtils.INSTANCES;
import static com.revolut.io.TestUtils.ROUND_ROBIN_INSTANCE;

public class RoundRobinSelectionStrategyTest {
    private SelectionStrategy selectionStrategy;

    @BeforeEach
    public void beforeEach() {
        this.selectionStrategy
                = new RoundRobinSelectionStrategy();
    }

    @Test
    public void selectInstance_ReturnInstanceInRoundRobin() {
        // When & Then
        for (var instance : ROUND_ROBIN_INSTANCE) {
            var actual = this.selectionStrategy.selectInstance(INSTANCES);
            Assertions.assertEquals(
                    instance,
                    actual
            );
        }
    }
}
