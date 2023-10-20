package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDto {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
}
