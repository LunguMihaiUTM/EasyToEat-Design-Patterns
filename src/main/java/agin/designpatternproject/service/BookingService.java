package agin.designpatternproject.service;
import agin.designpatternproject.entity.Booking;

import java.util.List;

public interface BookingService {
    String reorder(Long bookingId);
    List<Booking> getUserBookings(Long userId);
    String nextState(Long bookingId);
    String cancelBooking(Long bookingId);
}
