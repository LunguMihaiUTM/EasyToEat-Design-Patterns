package agin.designpatternproject.observer;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.entity.User;

public interface BookingObserver {
    void onBookingCreation(User user, Booking booking);
}
