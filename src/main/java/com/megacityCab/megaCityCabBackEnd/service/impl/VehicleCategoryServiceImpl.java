package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.entity.VehicleCategory;
import com.megacityCab.megaCityCabBackEnd.repo.VehicleCategoryRepo;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryDetailsService;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    @Autowired
    VehicleCategoryRepo categoryRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VehicleCategoryDetailsService categoryDetailsService;


    @Override
    public void saveCategory(String categoryName, long vehicleId) {
        VehicleCategory byCategory = categoryRepo.findByCategory(categoryName);
        if (Objects.equals(byCategory,null)){
            VehicleCategory category = new VehicleCategory(
                    (long)0,
                    categoryName,
                    "1"
            );
            VehicleCategory save = categoryRepo.save(category);
            categoryDetailsService.saveCategoryDetails(save.getCategoryId(),vehicleId);
        }
        else {
            categoryDetailsService.saveCategoryDetails(byCategory.getCategoryId(),vehicleId);
        }
    }

    @Override
    public void updateCategory(String categoryName, long vehicleId) {
        VehicleCategory byCategory = categoryRepo.findByCategory(categoryName);
        if (Objects.equals(byCategory,null)){
            VehicleCategory category = new VehicleCategory(
                    (long)0,
                    categoryName,
                    "1"
            );
            VehicleCategory save = categoryRepo.save(category);
            categoryDetailsService.updateCategoryDetails(save.getCategoryId(),vehicleId);
        }
        else {
            categoryDetailsService.updateCategoryDetails(byCategory.getCategoryId(),vehicleId);
        }
    }

    @Override
    public List<String> getAllCategory(String type) {
        return List.of();
    }
}
