package com.revolut.io.strategies.impl;

import com.revolut.io.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.revolut.io.TestUtils.INSTANCES;
import static com.revolut.io.TestUtils.WEIGHTED_ROUND_ROBIN_INSTANCE;

public class WeightedRoundRobinSelectionStrategyTest {
    private SelectionStrategy selectionStrategy;

    @BeforeEach
    public void beforeEach() {
        this.selectionStrategy = new WeightedRoundRobinSelectionStrategy();
    }

    @Test
    public void selectInstance_ReturnWeightedRoundRobinInstance() {
        for(var expected: WEIGHTED_ROUND_ROBIN_INSTANCE) {
            var actual = this.selectionStrategy.selectInstance(
                    INSTANCES
            );

            Assertions.assertEquals(
                    expected,
                    actual
            );
        }
    }
}
