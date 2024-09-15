package com.revolut.io.strategies.impl;

import com.revolut.io.model.BackendInstance;
import com.revolut.io.strategies.SelectionStrategy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSelectionStrategy implements SelectionStrategy {
    @Override
    public BackendInstance selectInstance(List<BackendInstance> instances) {
        return instances.get(
                ThreadLocalRandom.current().nextInt(instances.size())
        );
    }
}
