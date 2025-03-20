package com.megacityCab.megaCityCabBackEnd.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "vehicleId")
    private Long vehicleId;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "pickUpLocation")
    private String pickUpLocation;

    @Column(name = "dropLocation")
    private String dropLocation;

    @Column(name = "hours")
    private String hours;

    @Column(name = "totalKm")
    private Double totalKm;  // Changed from `double` to `Double`

    @Column(name = "bookingDateTime")
    private Date bookingDateTime;

    @Column(name = "totalAmount")
    private Double totalAmount;  // Changed from `double` to `Double`

    @Column(name = "estimatedBookingDateTime")
    private Date estimatedBookingDateTime;

    @Column(name = "status")
    private String status;
}

