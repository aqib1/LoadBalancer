package com.revolut.io.exceptions;

public class EmptyLoadBalancerException extends RuntimeException {
    public EmptyLoadBalancerException(String message) {
        super(message);
    }
}
