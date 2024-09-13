package com.revolut.io.domain;

import com.revolut.io.domain.model.BackendInstance;
import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

public class LoadBalancerTest {
    private static final int CAPACITY = 10;
    private LoadBalancer loadBalancer;

    @BeforeEach
    public void beforeEach() {
        this.loadBalancer = new LoadBalancer();
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addInstance_WhenLoadBalancerHaveCapacity_ReturnTrue() {
        // Given
        var backendInstance = new BackendInstance("127.0.0.1");

        // When & Then
        Assertions.assertTrue(
                this.loadBalancer.addInstance(backendInstance)
        );
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addInstance_WhenLoadBalancerHaveCapacity_InstanceIsAlreadyAdded_ReturnFalse() {
        // Given
        var backendInstance = new BackendInstance("127.0.0.1");

        // When & Then
        Assertions.assertTrue(
                this.loadBalancer.addInstance(backendInstance)
        );

        Assertions.assertFalse(
                this.loadBalancer.addInstance(backendInstance)
        );
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addInstance_WhenLoadBalancerHaveNoCapacity_ThrowException() {
        // Given & When
        addInstancesToLoadBalancer();

        // When & Then
        Assertions.assertThrows(LoadBalancerOverflowException.class, () ->
                this.loadBalancer.addInstance(new BackendInstance(
                        "127.0.0.22"
                )));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void removeInstance_WhenInstanceFoundAndRemoved_ReturnTrue() {
        // Given & When
        addInstancesToLoadBalancer();

        // When & Then
        Assertions.assertTrue(
                this.loadBalancer.removeInstance(
                        new BackendInstance("127.0.0.1")
                )
        );
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void removeInstance_WhenLoadBalancerEmpty_ShouldThrowException() {
        Assertions.assertThrows(
                EmptyLoadBalancerException.class,
                () -> this.loadBalancer.removeInstance(
                        new BackendInstance("127.0.0.1")
                )
        );
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void removeInstance_WhenInstanceIsAlreadyRemoved_ShouldReturnFalse() {
        // Given & When
        addInstancesToLoadBalancer();

        // When & Then
        Assertions.assertTrue(
                this.loadBalancer.removeInstance(
                        new BackendInstance("127.0.0.1")
                )
        );

        Assertions.assertFalse(
                this.loadBalancer.removeInstance(
                        new BackendInstance("127.0.0.1")
                )
        );
    }

    private void addInstancesToLoadBalancer() {
        for (int i = 0; i < CAPACITY; i++) {
            this.loadBalancer.addInstance(new BackendInstance(
                    "127.0.0." + i
            ));
        }
    }
}
