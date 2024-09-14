package com.revolut.io.domain;

import com.revolut.io.domain.model.BackendInstance;
import com.revolut.io.domain.strategies.SelectionStrategy;
import com.revolut.io.exceptions.EmptyLoadBalancerException;
import com.revolut.io.exceptions.LoadBalancerOverflowException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoadBalancerTest {
    private static final int CAPACITY = 10;
    private LoadBalancer loadBalancer;
    private final SelectionStrategy selectionStrategy = mock(SelectionStrategy.class);

    @BeforeEach
    public void beforeEach() {
        this.loadBalancer = new LoadBalancer(
                CAPACITY,
                selectionStrategy
        );
    }

    @Test
    public void addInstance_WhenLoadBalancerHaveCapacity_ReturnTrue() {
        // Given
        var backendInstance = new BackendInstance("127.0.0.1");

        // When & Then
        Assertions.assertTrue(
                this.loadBalancer.addInstance(backendInstance)
        );
    }

    @Test
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
    public void addInstance_WhenNullInstanceProvided_ThrowException() {
        // When & Then
        Assertions.assertThrows(NullPointerException.class, () ->
                this.loadBalancer.addInstance(null));
    }

    @Test
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
    public void removeInstance_WhenLoadBalancerEmpty_ShouldThrowException() {
        Assertions.assertThrows(
                EmptyLoadBalancerException.class,
                () -> this.loadBalancer.removeInstance(
                        new BackendInstance("127.0.0.1")
                )
        );
    }

    @Test
    public void removeInstance_WhenProvidedInstanceNull_ShouldThrowException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> this.loadBalancer.removeInstance(null)
        );
    }

    @Test
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

    @Test
    public void selectInstance_ShouldReturnNextInstance() {
        // Given & When
        var expectedInstance = "127.0.0.1";
        when(selectionStrategy.selectInstance(anyList()))
                .thenReturn(expectedInstance);

        // When
        addInstancesToLoadBalancer();
        var actualInstance = loadBalancer.selectInstance();
        Assertions.assertEquals(
                new BackendInstance(expectedInstance),
                actualInstance
        );
    }

    @Test
    public void selectInstance_WhenInstanceMapIsEmpty_ThrowException() {
        Assertions.assertThrows(
                EmptyLoadBalancerException.class,
                () -> loadBalancer.selectInstance()
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
