package agin.designpatternproject.service;

import agin.designpatternproject.dto.LocationDTO;
import agin.designpatternproject.dto.MenuDTO;
import agin.designpatternproject.dto.RestaurantDTO;
import agin.designpatternproject.dto.TableDTO;
import agin.designpatternproject.dto.request.RestaurantFilterDTO;
import agin.designpatternproject.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantResourceService {
    List<RestaurantDTO> getRestaurants(RestaurantFilterDTO filter);

    MenuDTO getMenuByRestaurantId(Long restaurantId);

    List<LocationDTO> getLocationsByRestaurantId(Long restaurantId);

    List<TableDTO> getFreeTablesByLocationId(Long locationId);

    Boolean isFreeTable(Long tableId);

    void updateTableFreeStatus(Long tableId, Boolean flag);

    Double calcAndGetPrice(List<Long> items);

    String getItemsToText(List<Long> items);

    void freeTable();

    Optional<Restaurant> findRestaurantByLocationId(Long locationId);
}