package com.cetin.hospital.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String message;
    Long userId;
    String accessToken;
    String refreshToken;
}
