package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDto {
    private Long vehicleId;
    private String plateNumber;
    private int passengerCount;
    private double pricePerKm;
    private String vehicleModel;
    private String status;
    private String image;
    private String category;
}
