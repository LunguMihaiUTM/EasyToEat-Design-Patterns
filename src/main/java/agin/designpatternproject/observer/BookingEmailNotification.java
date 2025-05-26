package agin.designpatternproject.observer;

import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.entity.Location;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.repository.LocationRepository;
import agin.designpatternproject.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEmailNotification implements BookingObserver {

    private final EmailService emailService;
    private final LocationRepository locationRepository;

    @Override
    public void onBookingCreation(User user, Booking booking) {
        String userEmail = user.getEmail();

        String locationName = locationRepository.findById(booking.getLocationId())
                .map(Location::getAddress)
                .orElse("necunoscută");

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Salut, ").append(user.getUsername()).append("!\n\n");
        messageBuilder.append("Rezervarea ta a fost efectuată cu succes. Detalii mai jos:\n\n");

        messageBuilder.append("Locație: ").append(locationName).append("\n");
        messageBuilder.append("Data comenzii: ").append(booking.getOrderTime()).append("\n");
        messageBuilder.append("Număr persoane: ").append(booking.getNoPeople()).append("\n\n");

        messageBuilder.append("Produse comandate:\n");
        for (String itemName : booking.getItems().split(",")) {
            messageBuilder.append("- ").append(itemName.trim()).append("\n");
        }

        messageBuilder
                .append("\nPreț total: ")
                .append(booking.getFinalPrice())
                .append(" MDL\n\n")
                .append("Îți mulțumim că ai ales EasyToEat!");

        String message = messageBuilder.toString();

        emailService.sendEmail(userEmail, "Detalii Rezervare - EasyToEat", message);
    }
}

