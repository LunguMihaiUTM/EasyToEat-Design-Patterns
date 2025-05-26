package agin.designpatternproject.controller;

import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.entity.Booking;
import agin.designpatternproject.facade.BookingFacade;
import agin.designpatternproject.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingFacade bookingFacade;

    @GetMapping("/reorder")
    public ResponseEntity<String> reorder(@RequestParam Long bookingId){
        return ResponseEntity.ok(bookingService.reorder(bookingId));
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody BookingDTO bookingDTO, @RequestParam String promoCode){
        return ResponseEntity.ok(bookingFacade.createBooking(bookingDTO, promoCode));
    }

}
