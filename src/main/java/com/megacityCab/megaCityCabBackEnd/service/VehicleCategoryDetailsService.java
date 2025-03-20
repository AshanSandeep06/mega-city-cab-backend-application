package com.megacityCab.megaCityCabBackEnd.service;

public interface VehicleCategoryDetailsService {

    void saveCategoryDetails(long categoryId,long vehicleId);
    void updateCategoryDetails(long categoryId,long vehicleId);
    boolean deleteCategoryDetails(long vehicleId);
}
