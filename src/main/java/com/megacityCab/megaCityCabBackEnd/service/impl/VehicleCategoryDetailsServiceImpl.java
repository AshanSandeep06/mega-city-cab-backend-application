package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.entity.VehicleCategoryDetails;
import com.megacityCab.megaCityCabBackEnd.repo.VehicleCategoryDetailsRepo;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Transactional
@Service
public class VehicleCategoryDetailsServiceImpl implements VehicleCategoryDetailsService {

    @Autowired
    VehicleCategoryDetailsRepo categoryDetailsRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void saveCategoryDetails(long categoryId, long vehicleId) {
        VehicleCategoryDetails categoryDetails = new VehicleCategoryDetails(
                (long)0,
                categoryId,
                vehicleId,
                "1"
        );
        categoryDetailsRepo.save(categoryDetails);
    }

    @Override
    public void updateCategoryDetails(long categoryId, long vehicleId) {
        boolean isDeleted = deleteCategoryDetails(vehicleId);
        if(isDeleted){
            VehicleCategoryDetails categoryDetails = new VehicleCategoryDetails(
                    (long)0,
                    categoryId,
                    vehicleId,
                    "1"
            );
            categoryDetailsRepo.save(categoryDetails);
        }
    }

    @Override
    public boolean deleteCategoryDetails(long vehicleId) {
        VehicleCategoryDetails byCategoryDetailsVehicleId = categoryDetailsRepo.findByCategoryDetailsVehicleId(vehicleId);
        if(!Objects.equals(byCategoryDetailsVehicleId,null)){
            categoryDetailsRepo.delete(byCategoryDetailsVehicleId);
            return true;
        }
        return false;
    }
}
