package com.revolut.io.domain;

import com.revolut.io.domain.model.BackendInstance;
import com.revolut.io.domain.strategies.SelectionStrategy;
import com.revolut.io.domain.strategies.impl.RandomSelectionStrategy;
import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {
    private static final int CAPACITY = 10;
    private final int capacity;
    private final Map<String, BackendInstance> instances;
    private final SelectionStrategy selectionStrategy;

    public LoadBalancer() {
        this(CAPACITY, new RandomSelectionStrategy());
    }

    public LoadBalancer(int capacity, SelectionStrategy selectionStrategy) {
        this.capacity = capacity;
        this.instances = new ConcurrentHashMap<>();
        this.selectionStrategy = selectionStrategy;
    }

    public boolean addInstance(BackendInstance backendInstance) {
        Objects.requireNonNull(backendInstance);

        if (isLoadBalancerOverflow())
            throw new LoadBalancerOverflowException("LoadBalancer does not have more capacity");

        return this.instances.putIfAbsent(
                backendInstance.address(),
                backendInstance
        ) == null;
    }

    public BackendInstance selectInstance() {
        if(isLoadBalancerEmpty())
            throw new EmptyLoadBalancerException("Load balancer is empty");

        var instances = this.instances.keySet().stream().toList();
        return this.instances.get(
                this.selectionStrategy.selectInstance(instances)
        );
    }

    private boolean isLoadBalancerOverflow() {
        return instances.size() == this.capacity;
    }

    public boolean removeInstance(BackendInstance backendInstance) {
        Objects.requireNonNull(backendInstance);

        if(isLoadBalancerEmpty())
            throw new EmptyLoadBalancerException("Load balancer is empty");

        return instances.remove(
                backendInstance.address()
        ) != null;
    }

    private boolean isLoadBalancerEmpty() {
        return this.instances.isEmpty();
    }
}
