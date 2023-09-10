package com.orienting.common.exception;

public class NoExistedUserWithUserId extends RuntimeException{
    public NoExistedUserWithUserId(String message) {
        super(message);
    }

}
