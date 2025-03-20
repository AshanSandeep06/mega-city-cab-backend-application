package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.dto.BookingDto;
import com.megacityCab.megaCityCabBackEnd.entity.Booking;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomBookingResult;

import java.util.List;

public interface BookingService {
    Booking saveBooking(BookingDto booking, String type);
    Booking updateBookingStatus(long bookingId, String type);
    Booking updateStatusNotConfirmBookingWherePaymentId(long paymentId,String type);
    int getPendingCount(String type);
    Booking getBookingById(Long bookingId, String type);
    List<CustomBookingDetails> getBookingDetails(String status, String type);
    List<CustomBookingResult> getAllBookingByCustomer(long userId, String type);
}
