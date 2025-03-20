package com.megacityCab.megaCityCabBackEnd.service;

import java.util.List;

public interface VehicleCategoryService {
    void saveCategory(String categoryName,long vehicleId);
    void updateCategory(String categoryName,long vehicleId);
    List<String> getAllCategory(String type);
}
