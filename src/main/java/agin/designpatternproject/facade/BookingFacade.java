package agin.designpatternproject.facade;

import agin.designpatternproject.decorator.Discount;
import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.entity.Item;
import agin.designpatternproject.enums.Status;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.factory.DiscountFactory;
import agin.designpatternproject.service.RestaurantResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingFacade {

    private final ItemExistenceService itemExistenceService;
    private final TableAvailabilityService tableAvailabilityService;
    private final PriceCalculationService priceCalculationService;
    private final BookingPersistenceService bookingPersistenceService;
    private final RestaurantResourceService restaurantResourceService;

    public String createBooking(BookingDTO dto, String promoCode) {

        tableAvailabilityService.validateTableIsFree(dto.getTableId());

        if(!itemExistenceService.verifyItems(dto.getItemIds()))
            throw new RestaurantResourceException("Some Items are not available");

        double totalPrice = priceCalculationService.calculateTotalPrice(dto.getItemIds());
        //apply discount
        Discount discount = DiscountFactory.getDiscountByPromoCode(promoCode);
        double finalPrice = discount.applyDiscount(totalPrice);

        Booking booking = Booking.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .mail(dto.getMail())
                .locationId(dto.getLocationId())
                .tableId(dto.getTableId())
                .noPeople(dto.getNoPeople())
                .items(restaurantResourceService.getItemsToText(dto.getItemIds()))
                .finalPrice(finalPrice)
                .bookingStatus(Status.IN_PROGRESS)
                .orderTime(LocalDateTime.now())
                .build();

        bookingPersistenceService.saveBooking(booking);
        return "Booking successfully created";
    }
}

