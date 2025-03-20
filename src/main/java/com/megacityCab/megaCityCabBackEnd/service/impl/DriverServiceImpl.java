package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.dto.DriverDto;
import com.megacityCab.megaCityCabBackEnd.entity.Driver;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import com.megacityCab.megaCityCabBackEnd.repo.DriverRepo;
import com.megacityCab.megaCityCabBackEnd.service.DriverService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {
    @Autowired
    DriverRepo driverRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Driver saveDriver(DriverDto dto, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Driver driverByEmail = driverRepo.getDriverByEmail(dto.getEmail());
        Driver driverByNic = driverRepo.getDriverByNic(dto.getNic());
        Driver driverByLicenseNumberNumber = driverRepo.getDriverByLicenseNumberNumber(dto.getLicenseNumber());
        Driver driverByContactNumber = driverRepo.getDriverByContactNumber(dto.getContactNumber());
        if(Objects.equals(driverByEmail,null)&&Objects.equals(driverByNic,null)&&
                Objects.equals(driverByLicenseNumberNumber,null)&&
                Objects.equals(driverByContactNumber,null)){
            Driver map = modelMapper.map(dto, Driver.class);
            return driverRepo.save(map);
        }
        throw new RuntimeException("driver already exist");
    }

    @Override
    public Driver updateDriver(DriverDto dto, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Driver driverById = driverRepo.getDriverById(dto.getDriverId());
        if(!Objects.equals(driverById,null)){
            Driver map = modelMapper.map(dto, Driver.class);
            return driverRepo.save(map);
        }
        throw new RuntimeException("driver not exist");
    }

    @Override
    public Driver deleteDriver(long driverId, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Driver driverById = driverRepo.getDriverById(driverId);
        if(!Objects.equals(driverById,null)){
            driverRepo.delete(driverById);
            return driverById;
        }
        throw new RuntimeException("driver not exist");
    }

    @Override
    public Driver getDriverById(long driverId,String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Driver driverById = driverRepo.getDriverById(driverId);
        if(!Objects.equals(driverById,null)){
            return driverById;
        }
        throw new RuntimeException("driver not exist");
    }

    @Override
    public Driver getDriverByEmail(String email,String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Driver driverByEmail = driverRepo.getDriverByEmail(email);
        if(!Objects.equals(driverByEmail,null)){
            return driverByEmail;
        }
        throw new RuntimeException("driver not exist");
    }

    @Override
    public List<DriverDto> getAllDriver(String status, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
//        return modelMapper.map(driverRepo.getAllDriver(),new TypeToken<List<DriverDto>>() {}.getType());

        List<DriverDto> allDrivers = modelMapper.map(driverRepo.getAllDriver(),new TypeToken<List<DriverDto>>() {}.getType());

        System.out.println("DRIVER STATUS: "+status);
        if (status.equalsIgnoreCase("All")) {
            return allDrivers;
        }

        return allDrivers.stream()
                .filter(driver -> driver.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public Driver getRandomlyDriver(String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        List<Driver> availableDriver = driverRepo.getAvailableDriver();
        if(availableDriver.isEmpty()){
            throw new NoSuchElementException("not have available drivers");

        }
        Random random = new Random();
        return availableDriver.get(random.nextInt(availableDriver.size()));
    }

    @Override
    public int getDriverCount(String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        return driverRepo.getAvailableDriverCount();
    }

    @Override
    public boolean changeStatusInDriver(long driverId) {
        Driver driverById = driverRepo.getDriverById(driverId);
        if(!Objects.equals(driverById,null) && driverById.getStatus().equals("Available")){
            driverById.setStatus("Busy");
            driverRepo.save(driverById);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatusInDriver(long driverId) {
        Driver driverById = driverRepo.getDriverById(driverId);
        if(!Objects.equals(driverById,null) && driverById.getStatus().equals("Busy")){
            driverById.setStatus("Available");
            driverRepo.save(driverById);
            return true;
        }
        return false;
    }

}
