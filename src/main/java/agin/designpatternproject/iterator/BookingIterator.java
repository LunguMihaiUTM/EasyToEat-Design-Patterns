package agin.designpatternproject.iterator;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;

public interface BookingIterator {
    boolean hasNext();
    Booking next();
}

