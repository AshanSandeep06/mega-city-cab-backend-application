package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.dto.PaymentDto;
import com.megacityCab.megaCityCabBackEnd.entity.Payment;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDateResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentMonthResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentResult;
import com.megacityCab.megaCityCabBackEnd.util.response.StripeResponse;

import java.util.List;


public interface PaymentService {
    StripeResponse createPayment(PaymentDto paymentDto, String type);
    Payment savePayment(PaymentDto paymentDto);
    List<CustomPaymentResult> getAllPayments(String type);
    List<CustomPaymentDateResult> getPaymentByThisWeekDay(String type);
    List<CustomPaymentMonthResult> getPaymentByThisMonth(String type);
    CustomPaymentDetails getPaymentDetailsByPaymentId(Long paymentId, String type, String reportFormat);
    byte[] returnExportReport(long paymentId,String reportFormat,String type);
    String getPaymentStatusById(long paymentId,String type);
}
