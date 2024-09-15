package com.revolut.io;

import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;
import com.revolut.io.model.BackendInstance;
import com.revolut.io.strategies.SelectionStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {
    private static final int CAPACITY = 10;
    private final SelectionStrategy selectionStrategy;
    private final Map<String, BackendInstance> instances;

    public LoadBalancer(SelectionStrategy selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
        this.instances = new ConcurrentHashMap<>();
    }

    public boolean addInstance(BackendInstance instance) {
        checkLoadBalancerOverflow();

        return instances.putIfAbsent(
                instance.address(),
                instance
        ) == null;
    }

    public boolean removeInstance(BackendInstance instance) {
        checkLoadBalancerIsEmpty();

        return instances.remove(
                instance.address()
        ) != null;
    }

    public BackendInstance selectInstance() {
        checkLoadBalancerIsEmpty();

        return this.selectionStrategy.selectInstance(
                instances.values().stream().toList()
        );
    }

    private void checkLoadBalancerIsEmpty() {
        if(instances.isEmpty())
            throw new EmptyLoadBalancerException("Load balancer is empty");
    }

    private void checkLoadBalancerOverflow() {
         if(this.instances.size() == CAPACITY)
             throw new LoadBalancerOverflowException("Load balancer is overflowed");
    }
}
