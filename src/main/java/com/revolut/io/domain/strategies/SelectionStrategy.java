package com.revolut.io.domain.strategies;

import com.revolut.io.exceptions.EmptyInstanceListException;

import java.util.List;
import java.util.Objects;

public interface SelectionStrategy {
    String selectInstance(List<String> instances);

    default void validateInstances(List<String> instances) {
        Objects.requireNonNull(instances);
        if(instances.isEmpty())
            throw new EmptyInstanceListException("Empty instance list not allowed");
    }
}
