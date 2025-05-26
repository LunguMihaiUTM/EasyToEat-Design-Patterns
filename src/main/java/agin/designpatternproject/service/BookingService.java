package agin.designpatternproject.service;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;

public interface BookingService {

    void validateAndBook(BookingDTO bookingdto);
    void finalizeBooking(Long bookingId, Status status);
    Double getBookingPrice(Long bookingId);
    String reorder(Long bookingId);
}
