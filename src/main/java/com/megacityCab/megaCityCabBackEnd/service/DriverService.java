package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.dto.DriverDto;
import com.megacityCab.megaCityCabBackEnd.entity.Driver;

import java.util.List;

public interface DriverService {
    Driver saveDriver(DriverDto dto, String type);
    Driver updateDriver(DriverDto dto, String type);
    Driver deleteDriver(long driverId,String type);
    Driver getDriverById(long driverId,String type);
    List<DriverDto> getAllDriver(String status, String type);
    boolean changeStatusInDriver(long driverId);
    boolean updateStatusInDriver(long driverId);
    Driver getRandomlyDriver(String type);
    int getDriverCount(String type);
    Driver getDriverByEmail(String email, String type);
}
