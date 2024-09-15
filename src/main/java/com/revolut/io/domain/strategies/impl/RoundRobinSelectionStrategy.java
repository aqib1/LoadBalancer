package com.revolut.io.strategies.impl;

import com.revolut.io.model.BackendInstance;
import com.revolut.io.strategies.SelectionStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelectionStrategy implements SelectionStrategy {
    private final AtomicInteger index;
    public RoundRobinSelectionStrategy() {
        this.index = new AtomicInteger(0);
    }
    @Override
    public BackendInstance selectInstance(List<BackendInstance> instances) {
        return instances.get(
                index.getAndUpdate(i -> (i + 1) % instances.size())
        );
    }
}
