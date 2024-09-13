package com.revolut.io.domain;

import com.revolut.io.domain.model.BackendInstance;
import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {
    private static final int CAPACITY = 10;
    private final int capacity;
    private final Map<String, BackendInstance> instances;

    public LoadBalancer() {
        this(CAPACITY);
    }

    public LoadBalancer(int capacity) {
        this.capacity = capacity;
        this.instances = new ConcurrentHashMap<>();
    }

    public boolean addInstance(BackendInstance backendInstance) {
        if (isLoadBalancerOverflow())
            throw new LoadBalancerOverflowException("LoadBalancer does not have more capacity");

        return this.instances.putIfAbsent(
                backendInstance.address(),
                backendInstance
        ) == null;
    }

    private boolean isLoadBalancerOverflow() {
        return instances.size() == this.capacity;
    }

    public boolean removeInstance(BackendInstance backendInstance) {
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
