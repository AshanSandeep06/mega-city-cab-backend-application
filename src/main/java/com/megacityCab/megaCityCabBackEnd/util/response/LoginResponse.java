package com.megacityCab.megaCityCabBackEnd.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int code;
    private String userName;
    private String message;
    private String role;
    private String jwt;
    private Long userId;
    private String email;
}
