package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.VehicleCategoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleCategoryDetailsRepo extends JpaRepository<VehicleCategoryDetails,Long> {

    @Query(value="select * from category_details where vehicle_id =:vehicleId and status='1'",nativeQuery = true)
    VehicleCategoryDetails findByCategoryDetailsVehicleId(@Param("vehicleId") long vehicleId);
}
