package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepo  extends JpaRepository<Driver,Long>{

    @Query(value = "select * from driver where driver_id=:driverId ",nativeQuery = true)
    Driver getDriverById(@Param("driverId")Long driverId);

    @Query(value = "select * from driver where email=:email ",nativeQuery = true)
    Driver getDriverByEmail(@Param("email")String email);

    @Query(value = "select * from driver where contact_number = :contactNumber ",nativeQuery = true)
    Driver getDriverByContactNumber(@Param("contactNumber")String contactNumber);

    @Query(value = "select * from driver where license_number = :licenseNumber  ",nativeQuery = true)
    Driver getDriverByLicenseNumberNumber(@Param("licenseNumber")String licenseNumber);

    @Query(value = "select * from driver where nic = :nic  and  status='Available' ",nativeQuery = true)
    Driver getDriverByNic(@Param("nic")String nic);

    @Query(value = "select * from driver  order by driver_id desc",nativeQuery = true)
    List<Driver> getAllDriver();

    @Query(value = "select count(*) from driver",nativeQuery = true)
    int getAvailableDriverCount();

    @Query(value = "select * from driver where status='Available'",nativeQuery = true)
    List<Driver> getAvailableDriver();

}
