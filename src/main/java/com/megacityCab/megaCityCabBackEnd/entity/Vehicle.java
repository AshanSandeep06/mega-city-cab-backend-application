package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "vehicleId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;
    @Column(name="plateNumber")
    @NonNull
    private String plateNumber;
    @Column(name="passengerCount")
    @NonNull
    private int passengerCount;
    @Column(name="pricePerKm")
    @NonNull
    private double pricePerKm;
    @Column(name = "vehicleModel")
    @NonNull
    private String vehicleModel;
    @Column(name = "status")
    @NonNull
    private String status;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @NonNull
    private String category;
}
