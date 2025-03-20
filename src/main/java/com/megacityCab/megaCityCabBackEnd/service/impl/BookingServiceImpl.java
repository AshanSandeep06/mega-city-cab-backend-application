package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.dto.BookingDto;
import com.megacityCab.megaCityCabBackEnd.entity.Booking;
import com.megacityCab.megaCityCabBackEnd.entity.Driver;
import com.megacityCab.megaCityCabBackEnd.entity.Payment;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingResult;
import com.megacityCab.megaCityCabBackEnd.repo.BookingRepo;
import com.megacityCab.megaCityCabBackEnd.repo.DriverRepo;
import com.megacityCab.megaCityCabBackEnd.repo.PaymentRepo;
import com.megacityCab.megaCityCabBackEnd.service.BookingService;
import com.megacityCab.megaCityCabBackEnd.service.DriverService;
import com.megacityCab.megaCityCabBackEnd.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    DriverRepo driverRepo;

    @Scheduled(cron = "0 * * * * ?")
    public void checkAndUpdateBookingStatus() {
        Date now = new Date();

        List<Booking> pendingBookings = bookingRepo.findByStatus();
        for (Booking booking : pendingBookings) {
            if (booking.getBookingDateTime().before(now)) {
                Booking bookingById = bookingRepo.getBookingById(booking.getBookingId());
                if (bookingById.getStatus().equals("Booking")) {
                    bookingById.setStatus("Pending");
                    vehicleService.changeVehicleStatus(booking.getVehicleId());
                    driverService.changeStatusInDriver(booking.getDriverId());
                    bookingRepo.save(bookingById);
                } else {
                    throw new RuntimeException("booking status not pending");
                }
            }
        }
    }


    @Override
    public Booking saveBooking(BookingDto bookingDto, String type) {
        if (!type.equals("User")) {
            throw new RuntimeException("Don't have permission");
        }

        List<Date> allBookingDatesByVehicleId = bookingRepo.getAllBookingDatesByVehicleId(bookingDto.getVehicleId());
        List<Booking> allBookingByVehicleId = bookingRepo.getAllBookingByVehicleId(bookingDto.getVehicleId());

        Date bookingDate = bookingDto.getBookingDateTime();
        double hoursToAdd = Double.parseDouble(bookingDto.getHours());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDate);

        int hours = (int) hoursToAdd;
        int minutes = (int) ((hoursToAdd - hours) * 60);

        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        Date updatedBookingDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Format the current booking date to compare only the date part
        String nowBookingDate = sdf.format(bookingDate);
        Driver driver = null;

        for (Booking bookings : allBookingByVehicleId) {
            // Format the existing booking date to compare only the date part
            String formattedDate = sdf.format(bookings.getBookingDateTime());

//            System.out.println("Formatted Date: " + formattedDate);
//            System.out.println("nowBookingDate Date: " + nowBookingDate);

            // Check if the dates are the same (ignoring time)
            if (formattedDate.equals(nowBookingDate) &&
                    bookings.getStatus().equals("Booking") ||
                    bookings.getStatus().equals("Pending") || bookings.getStatus().equals("Booking not close")){
                throw new RuntimeException("Cannot book the vehicle on the same day. The date is already booked.");
            }

        }

        List<Driver> availableDriver = driverRepo.getAvailableDriver();
        System.out.println("availableDriver: "+availableDriver);
        if(availableDriver.isEmpty()){
            throw new NoSuchElementException("not have available drivers");
        }
        Random random = new Random();
        driver = availableDriver.get(random.nextInt(availableDriver.size()));
        System.out.println("driver: "+driver);

        // If the date is not already booked, proceed to save the booking
        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());
        booking.setCustomerId(bookingDto.getCustomerId());
        booking.setVehicleId(bookingDto.getVehicleId());
        booking.setDriverId(driver.getDriverId());
        booking.setPickUpLocation(bookingDto.getPickUpLocation());
        booking.setDropLocation(bookingDto.getDropLocation());
        booking.setHours(bookingDto.getHours());
        booking.setTotalKm(bookingDto.getTotalKm());
        booking.setBookingDateTime(bookingDto.getBookingDateTime());
        booking.setTotalAmount(bookingDto.getTotalAmount());
        booking.setEstimatedBookingDateTime(updatedBookingDate); // Example value
        booking.setStatus("Booking Not Closed");

        return bookingRepo.save(booking);
    }

    @Override
    public Booking updateBookingStatus(long bookingId, String type) {
        if (!type.equals("Admin")) {
            throw new RuntimeException("Don't have permission");
        }

        System.out.println(bookingId);
        Booking bookingById = bookingRepo.getBookingById(bookingId);

        if (bookingById == null) {
            System.out.println("BOOKING NOT FOUND");
            throw new RuntimeException("Booking not found");
        }

        System.out.println(bookingById.getStatus());

        if (bookingById.getStatus().equals("Pending")) {
            boolean updatedVehicleStatus = vehicleService.updateVehicleStatus(bookingById.getVehicleId());
            boolean updatedDriverStatus = driverService.updateStatusInDriver(bookingById.getDriverId());

            if (updatedVehicleStatus && updatedDriverStatus) {
                bookingById.setStatus("Confirmed");
                return bookingRepo.save(bookingById);
            }
            throw new RuntimeException("Vehicle or driver status not updated");
        } else if (bookingById.getStatus().equals("Booking not close")) {
            return deleteBooking(bookingId);
        }

        throw new RuntimeException("Invalid booking status");
    }

    @Override
    public Booking updateStatusNotConfirmBookingWherePaymentId(long paymentId, String type) {
        if (!type.equals("User")) {
            throw new RuntimeException("dont have permission");
        }
        Payment paymentById = paymentRepo.getPaymentById(paymentId);
        if (!Objects.equals(paymentById, null)) {
            Booking bookingById = bookingRepo.getBookingById(paymentById.getBookingId());
            bookingById.setStatus("Booking");
            return bookingById;
        }
        throw new RuntimeException("payment not exist");
    }

    @Override
    public int getPendingCount(String type) {
        if (!type.equals("Admin")) {
            throw new RuntimeException("dont have permission");
        }
        return bookingRepo.getPendingCount();
    }

    @Override
    public Booking getBookingById(Long bookingId, String type) {
        if (!"Admin".equalsIgnoreCase(type) && !"User".equalsIgnoreCase(type)) {
            throw new AccessDeniedException("Insufficient permissions");
        }
        Booking booking = bookingRepo.getBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking with ID " + bookingId + " not found");
        }
        return booking;
    }

    private Booking deleteBooking(long bookingId) {
        Booking bookingById = bookingRepo.getBookingById(bookingId);
        Payment allPaymentByBookingId = paymentRepo.getAllPaymentByBookingId(bookingId);

        if (bookingById == null) {
            throw new RuntimeException("Booking not found");
        }

        if (allPaymentByBookingId != null) {
            paymentRepo.delete(allPaymentByBookingId);
        }

        bookingRepo.delete(bookingById);
        return bookingById;
    }

    @Override
    public List<CustomBookingDetails> getBookingDetails(String status, String type) {
        if (!type.equals("Admin")) {
            throw new RuntimeException("dont have permission");
        }
//        return bookingRepo.getBookingDetails();

        List<CustomBookingDetails> allBookings = bookingRepo.getBookingDetails();

        System.out.println("BOOKING STATUS: "+status);
        if (status.equalsIgnoreCase("All")) {
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> booking.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomBookingResult> getAllBookingByCustomer(long userId, String type) {
        if (!type.equals("User")){
            throw new RuntimeException("dont have permission");
        }
        return bookingRepo.getBookingByCustomerId(userId);
    }
}
