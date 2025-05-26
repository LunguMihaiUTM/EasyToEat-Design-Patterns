package agin.designpatternproject.state;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;

public class CanceledState implements BookingState {

    @Override
    public void next(Booking booking) {
        throw new IllegalStateException("Canceled booking cannot progress.");
    }

    @Override
    public void cancel(Booking booking) {
        // Already canceled
        throw new IllegalStateException("Booking is already canceled.");
    }

    @Override
    public Status getStatus() {
        return Status.CANCELED;
    }
}
