package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverDto {
    private Long driverId;
    private String name;
    private int age;
    private String email;
    private String licenseNumber;
    private String contactNumber;
    private String nic;
    private String address;
    private String status;
}