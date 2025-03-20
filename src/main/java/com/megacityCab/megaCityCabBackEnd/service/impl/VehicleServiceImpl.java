package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.dto.VehicleDto;
import com.megacityCab.megaCityCabBackEnd.entity.CustomQuery.VehiclecategoryCustomResult;
import com.megacityCab.megaCityCabBackEnd.entity.Vehicle;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import com.megacityCab.megaCityCabBackEnd.repo.VehicleRepo;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryDetailsService;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryService;
import com.megacityCab.megaCityCabBackEnd.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepo vehicleRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VehicleCategoryService categoryService;

    @Autowired
    VehicleCategoryDetailsService categoryDetailsService;


    @Override
    public Vehicle saveVehicle(VehicleDto vehicleDto, MultipartFile file, String type) {

        if (!type.equals("Admin")) {
            throw new BadCredentialsException("Don't have permission");
        }

        // Check if the vehicle with the given plate number already exists
        Vehicle byPlateNumber = vehicleRepo.findByPlateNumber(vehicleDto.getPlateNumber());
        if (Objects.isNull(byPlateNumber)) {
            try {
                System.out.println("MODEL MAPPING: "+vehicleDto);
                // Map DTO to Entity
                Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);

                // Convert image to Base64 string
                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

                // Set the Base64 image to the vehicle entity
                vehicle.setImage(base64Image);

                // Save vehicle
                Vehicle savedVehicle = vehicleRepo.save(vehicle);

                // Save category
                categoryService.saveCategory(vehicleDto.getCategory(), savedVehicle.getVehicleId());

                return savedVehicle;

            } catch (IOException e) {
                throw new RuntimeException("Error processing the image", e);
            }
        }

        // Throw exception if vehicle already exists
        throw new RuntimeException("Vehicle already exists");
    }


    @Override
    public Vehicle updateVehicle(VehicleDto vehicleDto, MultipartFile file, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Vehicle byVehicleId = vehicleRepo.findByVehicleId(vehicleDto.getVehicleId());
        if (!Objects.equals(byVehicleId,null)){
            try{
                vehicleDto.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
                Vehicle map = modelMapper.map(vehicleDto, Vehicle.class);
                Vehicle save = vehicleRepo.save(map);
                categoryService.updateCategory(vehicleDto.getCategory(), save.getVehicleId());
                return save;
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Vehicle isn't already exists");
    }

    @Override
    public List<VehicleCustomResult> getAllVehiclesWithCategory(String status, String type) {
//        if (!type.equals("Admin")) {
//            throw new BadCredentialsException("Don't have permission");
//        }
//        return vehicleRepo.getAllVehiclesWithCategory();

        List<VehicleCustomResult> allVehiclesWithCategory = vehicleRepo.getAllVehiclesWithCategory();

        System.out.println("VEHICLE STATUS: "+status);
        if (status.equalsIgnoreCase("All")) {
            return allVehiclesWithCategory;
        }

        System.out.println("Vehicles: "+allVehiclesWithCategory);

        return allVehiclesWithCategory.stream()
                .filter(vehicle -> vehicle.getVehicleStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());

    }

    @Override
    public Vehicle deleteVehicle(long vehicleId, String type) {
        if (!type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        Vehicle byVehicleId = vehicleRepo.findByVehicleId(vehicleId);
        if(!Objects.equals(byVehicleId,null)){
            boolean isDeleted = categoryDetailsService.deleteCategoryDetails(vehicleId);
            if(isDeleted){
                vehicleRepo.delete(byVehicleId);
                return byVehicleId;
            }
            throw new RuntimeException("Vehicle is not deleted");
        }
        throw new RuntimeException("Vehicle not exist");
    }

    @Override
    public List<String> getVehicleModelByCategoryName(String categoryName, String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        return vehicleRepo.getVehicleModelByCategoryName(categoryName);
    }

    @Override
    public int getVehicleCount(String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        return vehicleRepo.getVehicleCount();
    }

    @Override
    public Vehicle randomlyGetVehicle(String ModelName, String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        List<Vehicle> byVehicleModel = vehicleRepo.findByVehicleModel(ModelName);
        if (byVehicleModel.isEmpty()) {
            throw new NoSuchElementException("No vehicle found for the given model: " + ModelName);
        }
        Random random = new Random();
        return byVehicleModel.get(random.nextInt(byVehicleModel.size()));
    }

    @Override
    public boolean changeVehicleStatus(long vehicleId) {
        Vehicle byVehicleId = vehicleRepo.findByVehicleId(vehicleId);
        if (!Objects.equals(byVehicleId,null) && byVehicleId.getStatus().equals("Available")){
            byVehicleId.setStatus("Booking");
            vehicleRepo.save(byVehicleId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateVehicleStatus(long vehicleId) {
        Vehicle byVehicleId = vehicleRepo.findByVehicleId(vehicleId);
        if (!Objects.equals(byVehicleId,null) && byVehicleId.getStatus().equals("Booking")){
            byVehicleId.setStatus("Available");
            vehicleRepo.save(byVehicleId);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllVehicleModel(String type) {
        if (!type.equals("User")){
            throw new BadCredentialsException("dont have permission");
        }
        return vehicleRepo.getAllVehicleModel();
    }


}
