package com.orienting.service.exception;

public class NoExistedUserException extends RuntimeException{
    public NoExistedUserException(String message) {
        super(message);
    }
}
