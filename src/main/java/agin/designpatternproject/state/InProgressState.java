package agin.designpatternproject.state;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;

public class InProgressState implements BookingState {

    @Override
    public void next(Booking booking) {
        booking.setBookingStatus(Status.COMPLETED);
    }

    @Override
    public void cancel(Booking booking) {
        booking.setBookingStatus(Status.CANCELED);
    }

    @Override
    public Status getStatus() {
        return Status.IN_PROGRESS;
    }
}
