package com.orienting.service.exception;

public class InvalidRoleException extends IllegalArgumentException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
