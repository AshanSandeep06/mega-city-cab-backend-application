package com.megacityCab.megaCityCabBackEnd.entity.custom;

public interface VehicleCustomResult {
    long getVehicleId();
    String getModel();
    String getPlateNumber();
    String getCategory();
    double getPricePerKm();
    int getPassengerCount();
    String getImage();
    String getVehicleStatus(); // Ensure correct mapping for v.status
    String getCategoryStatus(); // Ensure correct mapping for c.status
}
