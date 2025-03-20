package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "vehicle_category")
public class VehicleCategory {
    @Id
    @Column(name = "categoryId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @NonNull
    @Column(name = "category")
    private String category;
    @NonNull
    @Column(name = "status")
    private String status;
}
