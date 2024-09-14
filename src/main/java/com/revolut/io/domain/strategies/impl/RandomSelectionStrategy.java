package com.revolut.io.domain.strategies.impl;

import com.revolut.io.domain.strategies.SelectionStrategy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSelectionStrategy implements SelectionStrategy {

    @Override
    public String selectInstance(List<String> instances) {
        validateInstances(instances);
        return instances.get(
                ThreadLocalRandom.current().nextInt(
                        instances.size()
                )
        );
    }
}
