package com.cetin.hospital.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String TC;
    private String password;
}
