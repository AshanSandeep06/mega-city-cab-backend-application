package com.megacityCab.megaCityCabBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDto {
    private Long paymentId;
    private Long bookingId;
    private double amount;
    private Date date;
    private String paymentMethod;
    private String currency;
    private Long customerId;
    private Long vehicleId;
    private String status;
}
