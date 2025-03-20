package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String contactNumber;
    private String email;
    private String address;
    private String nic;
    private String status;
    private String role;
}
