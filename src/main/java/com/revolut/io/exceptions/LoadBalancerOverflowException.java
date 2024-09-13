package com.revolut.io.exceptions;

public class LoadBalancerOverflowException extends RuntimeException {
    public LoadBalancerOverflowException(String message) {
        super(message);
    }
}
