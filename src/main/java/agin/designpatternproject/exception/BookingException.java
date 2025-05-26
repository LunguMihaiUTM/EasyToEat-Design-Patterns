package agin.designpatternproject.exception;

import java.io.Serial;

public class BookingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public BookingException() {
        super();
    }

    public BookingException(String message) {
        super(message);
    }
}
