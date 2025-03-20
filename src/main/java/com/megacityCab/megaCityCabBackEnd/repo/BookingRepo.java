package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.Booking;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    @Query(value = "select * from booking where  status ='Booking'", nativeQuery = true)
    List<Booking> findByStatus();

    @Query(value = "select * from booking where booking_id = :bookingId", nativeQuery = true)
    Booking getBookingById(@Param("bookingId") long bookingId);

    @Query(value = "select * from booking where vehicle_id=:vehicleId", nativeQuery = true)
    List<Booking> getAllBookingByVehicleId(@Param("vehicleId") long vehicleId);

    @Query(value = "select count(*) from booking where status='Pending'", nativeQuery = true)
    int getPendingCount();

    @Query(value = "select\n" +
            "    b.booking_id as bookingId,\n" +
            "    b.pick_up_location as pickupLocation,\n" +
            "    b.drop_location as dropLocation,\n" +
            "    b.driver_id as driverId,\n" +
            "    d.name as driverName,\n" +
            "    v.plate_number as vehiclePlateNumber,\n" +
            "    v.vehicle_model as vehicleModel,\n" +
            "    u.user_id as customerId,\n" +
            "    u.username as customerName,\n" +
            "    b.total_amount as amount,\n" +
            "    b.booking_date_time as bookingDate,\n" +
            "    b.status as status\n" +
            "from booking b\n" +
            "         join user u on b.customer_id = u.user_id\n" +
            "         join vehicle v on b.vehicle_id = v.vehicle_id\n" +
            "         join driver d on b.driver_id = d.driver_id order by b.booking_date_time desc", nativeQuery = true)
    List<CustomBookingDetails> getBookingDetails();

    @Query(value = "select booking_date_time from booking where vehicle_id=:vehicleId and status='Booking'", nativeQuery = true)
    List<Date> getAllBookingDatesByVehicleId(@Param("vehicleId") long vehicleId);

    @Query(value = "select\n" +
            "    b.booking_id as bookingId,\n" +
            "    b.pick_up_location as startLocation,\n" +
            "    b.drop_location as dropLocation,\n" +
            "    d.driver_id as driverId,\n" +
            "    d.name as driverName,\n" +
            "    v.plate_number as vehiclePlateNumber,\n" +
            "    v.vehicle_model as vehicleModel,\n" +
            "    b.total_amount as amount ,\n" +
            "    b.booking_date_time as bookingDate\n" +
            "from booking b\n" +
            "         join vehicle v on b.vehicle_id = v.vehicle_id\n" +
            "         join driver d on v.vehicle_id = d.driver_id\n" +
            "where b.customer_id = :customerId", nativeQuery = true)
    List<CustomBookingResult> getBookingByCustomerId(@Param("customerId") long customerId);
}
