package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleCategoryRepo extends JpaRepository<VehicleCategory,Long> {

    @Query(value = "select * from vehicle_category where category=:category",nativeQuery = true)
    VehicleCategory findByCategory(@Param("category") String category);
}
