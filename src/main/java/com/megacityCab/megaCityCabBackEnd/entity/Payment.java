package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "payment")
public class Payment {
    @Id
    @Column(name = "paymentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @NonNull
    @Column(name = "bookingId")
    private Long bookingId;
    @NonNull
    @Column(name = "amount")
    private double amount;
    @NonNull
    @Column(name = "paymentDate")
    private Date paymentDate;
    @NonNull
    @Column(name = "paymentMethod")
    private String paymentMethod;
    @NonNull
    @Column(name = "currency")
    private String currency;
    @NonNull
    @Column(name = "customerId")
    private Long customerId;
    @NonNull
    @Column(name = "vehicleId")
    private Long vehicleId;
    @NonNull
    @Column(name = "status")
    private String status;
}
