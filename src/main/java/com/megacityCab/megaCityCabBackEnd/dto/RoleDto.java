package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDto {
    private Long userRoleId;
    private String role;
    private String status;
}
