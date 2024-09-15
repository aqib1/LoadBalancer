package com.revolut.io.strategies.impl;

import com.revolut.io.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.revolut.io.TestUtils.INSTANCES;

public class RandomSelectionStrategyTest {
    private SelectionStrategy selectionStrategy;

    @BeforeEach
    public void beforeEach() {
        this.selectionStrategy = new RandomSelectionStrategy();
    }

    @Test
    public void selectInstance_ShouldReturnRandomInstance() {
        var instance = selectionStrategy.selectInstance(INSTANCES);
        Assertions.assertTrue(
                INSTANCES.contains(instance)
        );
    }
}
