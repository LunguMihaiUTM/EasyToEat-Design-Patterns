package agin.designpatternproject.state;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;

public class CompletedState implements BookingState {

    @Override
    public void next(Booking booking) {
        // Already completed, no next state
        throw new IllegalStateException("Booking is already completed.");
    }

    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Completed booking cannot be canceled.");
    }

    @Override
    public Status getStatus() {
        return Status.COMPLETED;
    }
}
