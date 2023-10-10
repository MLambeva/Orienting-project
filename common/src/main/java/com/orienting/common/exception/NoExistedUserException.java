package com.orienting.common.exception;

public class NoExistedUserException extends RuntimeException{
    public NoExistedUserException(String message) {
        super(message);
    }
}
