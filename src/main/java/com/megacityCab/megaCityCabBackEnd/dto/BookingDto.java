package com.megacityCab.megaCityCabBackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    private Long bookingId;
    private Long customerId;
    private Long vehicleId;
    private Long driverId;
    private String pickUpLocation;
    private String dropLocation;
    private String hours;
    private double totalKm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "UTC")
    private Date bookingDateTime;
    private double totalAmount;
    private String status;
}
