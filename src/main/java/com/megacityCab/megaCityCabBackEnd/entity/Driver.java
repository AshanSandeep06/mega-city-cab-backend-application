package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "driver")
public class Driver {

    @Id
    @Column(name = "driverId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    @NonNull
    @Column(name = "name")
    private String name;
    @NonNull
    @Column(name = "age")
    private int age;
    @NonNull
    @Column(name = "email")
    private String email;
    @NonNull
    @Column(name = "licenseNumber")
    private String licenseNumber;
    @NonNull
    @Column(name = "contactNumber")
    private String contactNumber;
    @NonNull
    @Column(name = "nic")
    private String nic;
    @NonNull
    @Column(name = "address")
    private String address;
    @NonNull
    @Column(name = "status")
    private String status;
}
