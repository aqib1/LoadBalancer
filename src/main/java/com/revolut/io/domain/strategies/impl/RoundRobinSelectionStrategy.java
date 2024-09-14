package com.revolut.io.domain.strategies.impl;

import com.revolut.io.domain.strategies.SelectionStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelectionStrategy implements SelectionStrategy {
    private final AtomicInteger index;

    public RoundRobinSelectionStrategy() {
        this.index = new AtomicInteger(0);
    }

    @Override
    public String selectInstance(List<String> instances) {
        return instances.get(
                this.index.getAndUpdate(i -> (i + 1) % instances.size())
        );
    }
}
