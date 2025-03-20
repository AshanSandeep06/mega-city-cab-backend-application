package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.CustomQuery.VehiclecategoryCustomResult;
import com.megacityCab.megaCityCabBackEnd.entity.Vehicle;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Long> {

    @Query(value = "select * from vehicle where plate_number=:plateNumber",nativeQuery = true)
    Vehicle findByPlateNumber(@Param("plateNumber") String plateNumber);

    @Query(value = "select * from vehicle where vehicle_id=:vehicleId",nativeQuery = true)
    Vehicle findByVehicleId(@Param("vehicleId") long vehicleId);

//    @Query(value = "select  v.vehicle_id as VehicleId, v.plate_number as PlateNumber , v.passenger_count as PassengerCount, v.price_per_km as  PricePerKm, v.vehicle_model as Model,v.status as vehicleStatus, v.image as Image,c.category as Category,c.status as categoryStatus from vehicle v left join category_details cd on v.vehicle_id = cd.vehicle_id left join vehicle_category c on cd.category_id = c.category_id where v.status='Available' order by  v.vehicle_id desc",nativeQuery = true)
@Query(value = "SELECT v.vehicle_id AS VehicleId, " +
        "v.plate_number AS PlateNumber, " +
        "v.passenger_count AS PassengerCount, " +
        "v.price_per_km AS PricePerKm, " +
        "v.vehicle_model AS Model, " +
        "v.status AS VehicleStatus, " +  // Ensure alias matches interface
        "v.image AS Image, " +
        "c.category AS Category, " +
        "c.status AS CategoryStatus " + // Ensure alias matches interface
        "FROM vehicle v " +
        "LEFT JOIN category_details cd ON v.vehicle_id = cd.vehicle_id " +
        "LEFT JOIN vehicle_category c ON cd.category_id = c.category_id " +
//        "WHERE v.status = 'Available' " +
        "ORDER BY v.vehicle_id DESC",
        nativeQuery = true)
    List<VehicleCustomResult> getAllVehiclesWithCategory();

    @Query(value = "SELECT DISTINCT v.vehicle_model\n" +
            "FROM vehicle v\n" +
            "         JOIN category_details cd ON v.vehicle_id = cd.vehicle_id\n" +
            "         JOIN category c ON cd.category_id = c.category_id\n" +
            "WHERE c.category = :categoryName\n" +
            "  AND EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM vehicle v2\n" +
            "    WHERE v2.vehicle_model = v.vehicle_model\n" +
            "      AND v2.status = 'Available'\n" +
            ");",nativeQuery = true)
    List<String> getVehicleModelByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "select count(*) from vehicle",nativeQuery = true)
    int getVehicleCount();

    @Query(value = "select * from vehicle where vehicle_model=:vehicleModel  and status='Available'",nativeQuery = true)
    List<Vehicle> findByVehicleModel(@Param("vehicleModel") String vehicleModel);

    @Query(value = "select distinct  vehicle_model from vehicle",nativeQuery = true)
    List<String> getAllVehicleModel();

}
