package com.megacityCab.megaCityCabBackEnd.util.response;

import com.megacityCab.megaCityCabBackEnd.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentResponse {
    String status;
    String message;
    String sessionId;
    String sessionUrl;
    Payment payment;
}
