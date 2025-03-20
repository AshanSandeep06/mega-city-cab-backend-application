package com.megacityCab.megaCityCabBackEnd.api;

import com.megacityCab.megaCityCabBackEnd.dto.BookingDto;
import com.megacityCab.megaCityCabBackEnd.entity.Booking;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingResult;
import com.megacityCab.megaCityCabBackEnd.service.BookingService;
import com.megacityCab.megaCityCabBackEnd.util.response.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;

    //    save booking
    @PostMapping(path = "/save")
    public ResponseEntity<StandardResponse> saveBooking(@RequestBody BookingDto dto,
                                                        @RequestAttribute String type){
        Booking booking = bookingService.saveBooking(dto, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"saved",booking),
                HttpStatus.OK
        );
    }

    @GetMapping("/viewHistory/{userId}")
    public ResponseEntity<StandardResponse> getAllBookingsById(
            @RequestAttribute String type, @PathVariable Long userId){
        List<CustomBookingResult> allBookingByCustomer = bookingService.getAllBookingByCustomer(userId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"get all bookings",allBookingByCustomer),
                HttpStatus.OK
        );
    }

    @PutMapping(params = {"pId"})
    public ResponseEntity<StandardResponse> updateStatusNotConfirmBookingWherePaymentId(@RequestParam long pId, @RequestAttribute String type){
        Booking booking = bookingService.updateStatusNotConfirmBookingWherePaymentId(pId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"updated status",booking),
                HttpStatus.OK
        );
    }

    //    get Pending booking count
    @GetMapping(path = "/pending/count")
    public ResponseEntity<StandardResponse>getPendingCount(@RequestAttribute String type) {
        int pendingCount = bookingService.getPendingCount(type);
        return new ResponseEntity<>(
                new StandardResponse(200,"get all  pending bookings count",pendingCount),
                HttpStatus.OK
        );
    }

    // New endpoint to fetch booking by ID
    @GetMapping("/view/{bookingId}")
    public ResponseEntity<StandardResponse> getBookingById(
            @PathVariable("bookingId") Long bookingId,
            @RequestAttribute String type) {
        Booking booking = bookingService.getBookingById(bookingId, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Booking retrieved successfully", booking),
                HttpStatus.OK
        );
    }

    //    get booking details
    @GetMapping(path = "/bookingDetails")
    public ResponseEntity<StandardResponse> getAllBookings( @RequestParam(value = "status", required = false, defaultValue = "all") String status, @RequestAttribute String type){
        List<CustomBookingDetails> bookingDetails = bookingService.getBookingDetails(status, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"get all bookings details",bookingDetails),
                HttpStatus.OK
        );
    }

    @PutMapping(params = {"bookingId"})
    public ResponseEntity<StandardResponse> updateBookingStatus(@RequestParam long bookingId,
                                                                @RequestAttribute String type) {
        Booking booking = bookingService.updateBookingStatus(bookingId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Booking Status Updated",booking),
                HttpStatus.OK
        );
    }
}
