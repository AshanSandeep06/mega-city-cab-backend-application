package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleCategoryDetailsDto {
    private Long categoryDetailsId;
    private Long categoryId;
    private Long vehicleId;
    private String status;
}
