package agin.designpatternproject.state;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;

public interface BookingState {
    void next(Booking booking);
    void cancel(Booking booking);
    Status getStatus();
}
