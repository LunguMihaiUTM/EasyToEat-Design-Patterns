package agin.designpatternproject.iterator;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;

import java.util.List;

public class BookingList implements BookingCollection {

    private final List<Booking> bookings;

    public BookingList(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public BookingIterator iterator() {
        return new BookingListIterator(bookings);
    }
}

