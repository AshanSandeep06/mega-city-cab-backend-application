package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.Payment;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDateResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentMonthResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {

    @Query(value = "SELECT\n" +
            "    p.payment_id as paymentId,\n" +
            "    p.booking_id as bookingId,\n" +
            "    p.amount as amount,\n" +
            "    p.payment_date as date,\n" +
            "    p.payment_method as paymentMethod,\n" +
            "    p.customer_id customerId,\n" +
            "    u.username AS customerName,\n" +
            "    p.vehicle_id as vehicleId,\n" +
            "    v.vehicle_model as vehicleModel,\n" +
            "    b.driver_id as driverId,\n" +
            "    d.name AS driverName\n" +
            "FROM\n" +
            "    payment p\n" +
            "        JOIN\n" +
            "    user u ON p.customer_id = u.user_id\n" +
            "        JOIN\n" +
            "    vehicle v ON p.vehicle_id = v.vehicle_id\n" +
            "        JOIN\n" +
            "    booking b ON p.booking_id = b.booking_id\n" +
            "        JOIN\n" +
            "    driver d ON b.driver_id = d.driver_id  order by p.booking_id desc",nativeQuery = true)
    List<CustomPaymentResult> getPayments();

    @Query(value = "SELECT\n" +
            "    DATE(payment_date) AS paymentDate,\n" +
            "    SUM(amount) AS totalAmount\n" +
            "FROM\n" +
            "    payment\n" +
            "WHERE\n" +
            "    payment_date >= CURDATE() - INTERVAL 7 DAY\n" +
            "GROUP BY\n" +
            "    DATE(payment_date)\n" +
            "ORDER BY\n" +
            "    paymentDate DESC;",nativeQuery = true)
    List<CustomPaymentDateResult> getPaymentByThisWeekDay();

    @Query(value = "\n" +
            "select\n" +
            "    DATE_FORMAT(payment_date, '%M') as monthName,  -- Get full month name (e.g., January, February)\n" +
            "    SUM(amount) AS totalAmount\n" +
            "from payment\n" +
            "where YEAR(payment_date) = YEAR(CURDATE())  -- Filters payments for the current year\n" +
            "group by MONTH(payment_date), DATE_FORMAT(payment_date, '%M')\n" +
            "order by  MONTH(payment_date) asc",nativeQuery = true)
    List<CustomPaymentMonthResult> getPaymentByThisMonth();

    @Query(value = "SELECT\n" +
            "    p.payment_id as paymentId,\n" +
            "    b.booking_id as bookingId,\n" +
            "    u.user_id as userId,\n" +
            "    u.username as userName,\n" +
            "    b.pick_up_location as start_location,\n" +
            "    b.drop_location as drop_location,\n" +
            "    b.driver_id as driverId,\n" +
            "    d.name AS driver_name,\n" +
            "    v.plate_number AS vehicle_plate_number,\n" +
            "    v.vehicle_model AS vehicle_model,\n" +
            "    b.total_km as totalKm,\n" +
            "    b.hours as hours,\n" +
            "    p.currency as currency,\n" +
            "    p.amount as ammount,\n" +
            "    p.payment_date as paymentDate\n" +
            "FROM payment p\n" +
            "         JOIN booking b ON p.booking_id = b.booking_id\n" +
            "         JOIN user u ON b.cutomer_id = u.user_id\n" +
            "         JOIN vehicle v ON b.vehicle_id = v.vehicle_id\n" +
            "         JOIN driver d ON b.driver_id = d.driver_id\n" +
            "WHERE p.payment_id = :paymentId",nativeQuery = true)
    CustomPaymentDetails getPaymentDetailsByPaymentId(@Param("paymentId") long paymentId);

    @Query(value = "select status from payment where payment_id =:paymentId",nativeQuery = true)
    String getPaymentStatusById(@Param("paymentId") long paymentId);

    @Query(value = "select * from payment where payment_id=:pId",nativeQuery = true)
    Payment getPaymentById(@Param("pId")long pId);

    @Query(value = "select * from payment where booking_id= :bookingId",nativeQuery = true)
    Payment getAllPaymentByBookingId(@Param("bookingId") long bookingId);


}
