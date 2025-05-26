package agin.designpatternproject.facade;

import agin.designpatternproject.command.MarkTableAsOccupiedCommand;
import agin.designpatternproject.command.TableAvailabilityCommand;
import agin.designpatternproject.decorator.Discount;
import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.entity.Table;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.enums.Status;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.factory.DiscountFactory;
import agin.designpatternproject.observer.BookingObserver;
import agin.designpatternproject.repository.TableRepository;
import agin.designpatternproject.repository.UserRepository;
import agin.designpatternproject.service.RestaurantResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BookingFacade {

    private final ItemExistenceService itemExistenceService;
    private final TableAvailabilityService tableAvailabilityService;
    private final PriceCalculationService priceCalculationService;
    private final BookingPersistenceService bookingPersistenceService;
    private final RestaurantResourceService restaurantResourceService;
    private final BookingObserver bookingObserver;

    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    public String createBooking(Long userId ,BookingDTO dto, String promoCode) {

        tableAvailabilityService.validateTableIsFree(dto.getTableId());

        if(!itemExistenceService.verifyItems(dto.getItemIds()))
            throw new RestaurantResourceException("Some Items are not available");

        double totalPrice = priceCalculationService.calculateTotalPrice(dto.getItemIds());
        //apply discount
        Discount discount = DiscountFactory.getDiscountByPromoCode(promoCode);
        double finalPrice = discount.applyDiscount(totalPrice);


        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestaurantResourceException("User not found")
        );

        Booking booking = Booking.builder()
                .user(user)
                .locationId(dto.getLocationId())
                .tableId(dto.getTableId())
                .noPeople(dto.getNoPeople())
                .items(restaurantResourceService.getItemsToText(dto.getItemIds()))
                .finalPrice(finalPrice)
                .bookingStatus(Status.IN_PROGRESS)
                .orderTime(LocalDateTime.now())
                .build();


        //Command
        Table table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new RestaurantResourceException("Table not found"));
        TableAvailabilityCommand markAsOccupied = new MarkTableAsOccupiedCommand(table, tableRepository);
        markAsOccupied.execute();

        //Observer - notify the user
        //bookingObserver.onBookingCreation(user, booking);

        bookingPersistenceService.saveBooking(booking);
        return "Booking successfully created";
    }
}

