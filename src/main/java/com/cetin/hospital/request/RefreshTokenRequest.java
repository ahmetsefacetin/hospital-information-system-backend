package com.cetin.hospital.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private Long userId;
    private String refreshToken;
}
