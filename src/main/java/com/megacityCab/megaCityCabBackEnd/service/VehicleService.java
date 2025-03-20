package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.dto.VehicleDto;
import com.megacityCab.megaCityCabBackEnd.entity.CustomQuery.VehiclecategoryCustomResult;
import com.megacityCab.megaCityCabBackEnd.entity.Vehicle;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle(VehicleDto vehicleDto, MultipartFile file,String type) ;
    Vehicle updateVehicle(VehicleDto vehicleDto, MultipartFile file,String type);
    List<VehicleCustomResult> getAllVehiclesWithCategory(String status, String type);
    Vehicle deleteVehicle(long vehicleId,String type);
    List<String> getVehicleModelByCategoryName(String categoryName,String type);
    int getVehicleCount(String type);
    Vehicle randomlyGetVehicle(String ModelName,String type);
    boolean changeVehicleStatus(long vehicleId);
    boolean updateVehicleStatus(long vehicleId);
    List<String> getAllVehicleModel(String type);
}
