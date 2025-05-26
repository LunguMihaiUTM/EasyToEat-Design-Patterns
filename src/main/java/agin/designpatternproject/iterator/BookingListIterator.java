package agin.designpatternproject.iterator;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;

import java.util.List;
import java.util.NoSuchElementException;

public class BookingListIterator implements BookingIterator {

    private final List<Booking> bookings;
    private int index = 0;

    public BookingListIterator(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean hasNext() {
        return index < bookings.size();
    }

    @Override
    public Booking next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more DTOs.");
        }
        return bookings.get(index++);
    }
}
