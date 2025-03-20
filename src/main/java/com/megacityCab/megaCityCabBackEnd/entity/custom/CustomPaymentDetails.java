package com.megacityCab.megaCityCabBackEnd.entity.custom;

import java.util.Date;

public interface CustomPaymentDetails {
    long getPaymentId();
    long getBookingId();
    long getUserId();
    String getUserName();
    String getStart_location();
    String getDrop_location();
    long getDriverId();
    String getDriver_name();
    String getVehicle_plate_number();
    String getVehicle_model();
    Double getTotalKm();
    String getHours();
    String getCurrency();
    Double getAmount();
    Date getPaymentDate();
}
