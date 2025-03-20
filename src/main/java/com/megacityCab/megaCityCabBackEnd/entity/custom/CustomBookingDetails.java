package com.megacityCab.megaCityCabBackEnd.entity.custom;

import java.util.Date;

public interface CustomBookingDetails {
    long getBookingId();
    String getPickupLocation();
    String getDropLocation();
    long getDriverId();
    String  getVehiclePlateNumber();
    String getVehicleModel();
    String getDriverName();
    long getCustomerId();
    String getCustomerName();
    Date getBookingDate();
    double getAmount();
    String getStatus();
}
