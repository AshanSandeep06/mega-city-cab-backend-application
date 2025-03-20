package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleCategoryDto {
    private Long categoryId;
    private String category;
    private String status;
}
