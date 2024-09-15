package com.revolut.io.strategies;

import com.revolut.io.model.BackendInstance;

import java.util.List;

public interface SelectionStrategy {
    BackendInstance selectInstance(List<BackendInstance> instances);
}
