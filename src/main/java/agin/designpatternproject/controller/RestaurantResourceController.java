package agin.designpatternproject.controller;

import agin.designpatternproject.dto.RestaurantDTO;
import agin.designpatternproject.dto.request.RestaurantFilterDTO;
import agin.designpatternproject.exception.BookingException;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.service.RestaurantResourceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurant-resource")
public class RestaurantResourceController {
    private final RestaurantResourceService restaurantResourceService;

    @PostMapping
    @Operation(summary = "Get Restaurants by filter(categoryID, name)", description = "Return a List of RestaurantDTO, " +
            " to get all restaurants make the request with null param")
    public ResponseEntity<List<RestaurantDTO>> getRestaurants(@RequestBody(required = false) RestaurantFilterDTO filter) {
        return ResponseEntity.ok(restaurantResourceService.getRestaurants(filter));
    }

    @GetMapping("/menu/{restaurantId}")
    @Operation(summary = "Get Menu by Restaurant Id", description = "Return a MenuDTO")
    public ResponseEntity<?> getMenuByRestaurantId(@PathVariable Long restaurantId) {
        try {
            return ResponseEntity.ok(restaurantResourceService.getMenuByRestaurantId(restaurantId));
        } catch (RestaurantResourceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("Service currently unavailable, please try a little later!");
        }
    }

    @GetMapping("/restaurants/has-free-table/{locationId}")
    @Operation(summary = "Verify if has free table", description = "Return a boolean var")
    public ResponseEntity<?> hasFreeTable(@PathVariable Long locationId) {
        try {
            return ResponseEntity.ok(restaurantResourceService.isFreeTable(locationId));
        } catch (BookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("Service currently unavailable, please try a little later!");
        }
    }


    @GetMapping("/locations/{restaurantId}")
    @Operation(summary = "Get locations by Restaurant Id", description = "Return a list of LocationDTO")
    public ResponseEntity<?> getLocationsByRestaurantId(@PathVariable Long restaurantId) {
        try {
            return ResponseEntity.ok(restaurantResourceService.getLocationsByRestaurantId(restaurantId));
        } catch (RestaurantResourceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("Service currently unavailable, please try a little later!");
        }
    }

    @GetMapping("/tables/{locationId}")
    @Operation(summary = "Get free tables by Location Id", description = "Returns free tables based of a location " +
            " based by locationId")
    public ResponseEntity<?> getFreeTablesByLocationId(@PathVariable Long locationId) {
        return ResponseEntity.ok(restaurantResourceService.getFreeTablesByLocationId(locationId));
    }
}
