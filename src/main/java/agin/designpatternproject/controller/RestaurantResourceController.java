package agin.designpatternproject.controller;

import agin.designpatternproject.dto.LocationDTO;
import agin.designpatternproject.dto.RestaurantDTO;
import agin.designpatternproject.dto.request.RestaurantFilterDTO;
import agin.designpatternproject.entity.Item;
import agin.designpatternproject.entity.Menu;
import agin.designpatternproject.entity.Restaurant;
import agin.designpatternproject.exception.BookingException;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.ItemRepository;
import agin.designpatternproject.repository.MenuRepository;
import agin.designpatternproject.repository.RestaurantRepository;
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
    private final RestaurantRepository restaurantRepository;
    private final ItemRepository itemRepository;
    private final MenuRepository menuRepository;

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

    @GetMapping("/restaurant-by-id")
    public RestaurantDTO getRestaurantById(@RequestParam Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new RestaurantResourceException("Restaurant with id " + restaurantId + " not found")
        );

        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .locations(restaurantResourceService.getLocationsByRestaurantId(restaurantId))
                .logo(restaurant.getLogo())
                .build();
    }

    @GetMapping("/verify-cart")
    public boolean verifyCart(@RequestParam List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return false;
        }
        Restaurant firstRestaurant = findByItemId(itemIds.get(0));

        for (int i = 1; i < itemIds.size(); i++) {
            Restaurant currentRestaurant = findByItemId(itemIds.get(i));
            if (!currentRestaurant.getId().equals(firstRestaurant.getId())) {
                return false;
            }
        }

        return true;
    }

    @GetMapping("/locations-by-item")
    public ResponseEntity<?> findLocationsByItemId(@RequestParam Long itemId){
        Restaurant restaurant = findByItemId(itemId);
        return getLocationsByRestaurantId(restaurant.getId());
    }


    private Restaurant findByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        Menu menu = menuRepository.findById(item.getMenuId()).orElseThrow();
        return restaurantRepository.findById(menu.getRestaurantId()).orElseThrow();
    }



}
