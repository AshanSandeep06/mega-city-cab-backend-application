package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "categoryDetails")
public class VehicleCategoryDetails {
    @Id
    @Column(name = "categoryDetailsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryDetailsId;
    @NonNull
    @Column(name = "categoryId")
    private Long categoryId;
    @NonNull
    @Column(name = "vehicleId")
    private Long vehicleId;
    @NonNull
    @Column(name = "status")
    private String status;
}
