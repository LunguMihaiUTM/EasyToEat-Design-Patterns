package agin.designpatternproject.service.impl;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;
import agin.designpatternproject.exception.BookingException;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.factory.BookingStateFactory;
import agin.designpatternproject.iterator.BookingCollection;
import agin.designpatternproject.iterator.BookingIterator;
import agin.designpatternproject.iterator.BookingList;
import agin.designpatternproject.repository.BookingRepository;
import agin.designpatternproject.service.BookingService;
import agin.designpatternproject.service.RestaurantResourceService;
import agin.designpatternproject.state.BookingState;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public String reorder(Long bookingId) {
        Booking original = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RestaurantResourceException("Booking not found"));

        Booking newBooking = original.clone();
        bookingRepository.save(newBooking);
        return "You have successfully reordered the booking!";
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);

        BookingCollection bookingCollection = new BookingList(bookings);
        BookingIterator iterator = bookingCollection.iterator();

        List<Booking> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

    @Override
    public String nextState(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RestaurantResourceException("Booking not found"));

        BookingState state = BookingStateFactory.getState(booking.getBookingStatus());
        state.next(booking);
        bookingRepository.save(booking);
        return "Booking moved to next state: " + booking.getBookingStatus();
    }

    @Override
    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RestaurantResourceException("Booking not found"));

        BookingState state = BookingStateFactory.getState(booking.getBookingStatus());
        state.cancel(booking);
        bookingRepository.save(booking);
        return "Booking was canceled.";
    }

}
