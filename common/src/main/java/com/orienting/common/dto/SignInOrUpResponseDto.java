package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInOrUpResponseDto {
    String status;
    String message;

    public SignInOrUpResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
