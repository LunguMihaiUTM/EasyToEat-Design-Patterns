package agin.designpatternproject.service.impl;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.enums.Status;
import agin.designpatternproject.exception.BookingException;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.BookingRepository;
import agin.designpatternproject.service.BookingService;
import agin.designpatternproject.service.RestaurantResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RestaurantResourceService restaurantResourceService;

    @Override
    @Transactional
    public void validateAndBook(BookingDTO bookingdto) {
        if (bookingdto.getTableId() != null && !restaurantResourceService.isFreeTable(bookingdto.getTableId())) {
            throw new BookingException("Table is not free, change it or try again later!");
        } else {
            restaurantResourceService.updateTableFreeStatus(bookingdto.getTableId(), false);
        }

        Booking booking = bookingRepository.save(
                Booking.builder()
                        .tableId(bookingdto.getTableId())
                        .locationId(bookingdto.getLocationId())
                        .bookingStatus(Status.IN_PROGRESS)
                        .noPeople(bookingdto.getNoPeople())
                        .finalPrice(restaurantResourceService.calcAndGetPrice(bookingdto.getItemIds()))
                        .name(bookingdto.getName())
                        .phoneNumber(bookingdto.getPhoneNumber())
                        .mail(bookingdto.getMail())
                        .items(bookingdto.getItemIds().toString())
                        .build()
        );
    }

    @Override
    @Transactional
    public void finalizeBooking(Long bookingId, Status status) {
        log.info("Finalizing booking: {} with status {}", bookingId, status);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            booking.get().setBookingStatus(status);
            bookingRepository.save(booking.get());
            restaurantResourceService.updateTableFreeStatus(booking.get().getTableId(), true);
        }
    }

    @Override
    public Double getBookingPrice(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            return booking.get().getFinalPrice();
        } else {
            throw new BookingException("This booking is not available anymore!");
        }
    }

    public String reorder(Long bookingId) {
        Booking original = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RestaurantResourceException("Booking not found"));

        Booking newBooking = original.clone();
        bookingRepository.save(newBooking);
        return "You have successfully reordered the booking!";
    }
}
