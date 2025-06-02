package agin.designpatternproject.controller;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.dto.request.PaymentRequest;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.enums.Role;
import agin.designpatternproject.exception.UserException;
import agin.designpatternproject.facade.BookingFacade;
import agin.designpatternproject.service.BookingService;
import agin.designpatternproject.util.TokenExtractor;
import agin.designpatternproject.util.UserExtractor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingFacade bookingFacade;
    private final UserExtractor userExtractor;
    private final TokenExtractor tokenExtractor;

    @GetMapping("/reorder")
    public ResponseEntity<String> reorder(@RequestParam Long bookingId){
        return ResponseEntity.ok(bookingService.reorder(bookingId));
    }


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    public ResponseEntity<String> create(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam String promoCode,
            @RequestParam String paymentType,
            @RequestHeader("Authorization") String authHeader){

        User user = userExtractor.getUser(tokenExtractor.getToken(authHeader));

        return ResponseEntity.ok(bookingFacade.createBooking(user.getId(), bookingDTO, promoCode, paymentType));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/user-bookings")
    public ResponseEntity<List<Booking>> getUserBookings(
            @RequestHeader("Authorization") String authHeader){
        User user = userExtractor.getUser(tokenExtractor.getToken(authHeader));
        return ResponseEntity.ok(bookingService.getUserBookings(user.getId()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{bookingId}/next")
    public ResponseEntity<String> moveToNext(
            @PathVariable Long bookingId,
            @RequestHeader("Authorization") String authHeader) {

        User user = userExtractor.getUser(tokenExtractor.getToken(authHeader));
        if(user.getRole()!= Role.ADMIN)
            throw new UserException("You are not allowed to move this booking");
        return ResponseEntity.ok(bookingService.nextState(bookingId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancel(
            @PathVariable Long bookingId,
            @RequestHeader("Authorization") String authHeader) {

        User user = userExtractor.getUser(tokenExtractor.getToken(authHeader));
        if(user.getRole()!= Role.ADMIN)
            throw new UserException("You are not allowed to move this booking");
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }



}
