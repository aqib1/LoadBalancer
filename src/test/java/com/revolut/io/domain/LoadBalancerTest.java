package com.revolut.io;

import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;
import com.revolut.io.model.BackendInstance;
import com.revolut.io.strategies.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoadBalancerTest {
    private static final int CAPACITY = 10;
    private final SelectionStrategy selectionStrategy = mock(SelectionStrategy.class);
    private LoadBalancer loadBalancer;

    @BeforeEach
    public void beforeEach() {
        this.loadBalancer = new LoadBalancer(selectionStrategy);
    }

    @Test
    public void addInstance_WhenLoadBalancerHaveCapacity_ReturnTrue() {
        // Given
        var instance = new BackendInstance("127.0.0.1", 5);

        // When & Then
        Assertions.assertTrue(
                loadBalancer.addInstance(instance)
        );
    }

    @Test
    public void addInstance_WhenLoadBalancerHaveCapacity_ReturnFalseIfAlreadyExists() {
        // Given
        var instance = new BackendInstance("127.0.0.1", 5);

        // When & Then
        Assertions.assertTrue(
                loadBalancer.addInstance(instance)
        );
        Assertions.assertFalse(
                loadBalancer.addInstance(instance)
        );
    }

    @Test
    public void addInstance_WhenLoadBalancerHaveNoCapacity_ShouldThrowException() {
        // Given
        addBackendInstances();

        // When & Then
        Assertions.assertThrows(
                LoadBalancerOverflowException.class,
                () -> loadBalancer.addInstance(new BackendInstance("127.0.0.11", 5))
        );
    }

    @Test
    public void removeInstance_WhenLoadBalancerHaveInstance_ReturnTrue() {
        // Given
        addBackendInstances();

        Assertions.assertTrue(
                loadBalancer.removeInstance(new BackendInstance("127.0.0.1", 5))
        );
    }

    @Test
    public void removeInstance_WhenLoadBalancerDoesNotHaveInstance_ReturnFalse() {
        // Given
        addBackendInstances();

        Assertions.assertFalse(
                loadBalancer.removeInstance(new BackendInstance("127.0.0.21", 5))
        );
    }

    @Test
    public void removeInstance_WhenLoadBalancerIsEmpty_ThrowException() {
        // When & Then
        Assertions.assertThrows(
                EmptyLoadBalancerException.class,
                () -> loadBalancer.removeInstance(new BackendInstance("127.0.0.11", 5))
        );
    }

    @Test
    public void selectInstance_WhenSelectionStrategyProvided_ReturnInstance() {
        // When & Then
        Assertions.assertThrows(
                EmptyLoadBalancerException.class,
                () -> loadBalancer.selectInstance()
        );
    }

    @Test
    public void selectInstance_WhenLoadBalancerIsEmpty_ThrowException() {
        // When
        addBackendInstances();
        var expected = new BackendInstance("127.0.0.1", 5);
        when(selectionStrategy.selectInstance(anyList()))
                .thenReturn(expected);

        // Then
        var actual = loadBalancer.selectInstance();
        Assertions.assertEquals(
                expected,
                actual
        );
    }

    private void addBackendInstances() {
        for (int i = 0; i < CAPACITY; i++) {
            loadBalancer.addInstance(new BackendInstance("127.0.0." + i, i));
        }
    }
}
