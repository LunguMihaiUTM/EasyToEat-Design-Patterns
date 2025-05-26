package agin.designpatternproject.factory;

import agin.designpatternproject.enums.Status;
import agin.designpatternproject.state.BookingState;
import agin.designpatternproject.state.CanceledState;
import agin.designpatternproject.state.CompletedState;
import agin.designpatternproject.state.InProgressState;

public class BookingStateFactory {

    public static BookingState getState(Status status) {
        return switch (status) {
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED -> new CompletedState();
            case CANCELED -> new CanceledState();
        };
    }
}