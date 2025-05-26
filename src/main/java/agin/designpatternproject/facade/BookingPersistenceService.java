package agin.designpatternproject.facade;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingPersistenceService {

    private final BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}
