package agin.designpatternproject.service.impl;

import agin.designpatternproject.dto.*;
import agin.designpatternproject.dto.request.RestaurantFilterDTO;
import agin.designpatternproject.entity.*;
import agin.designpatternproject.exception.BookingException;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.*;
import agin.designpatternproject.service.RestaurantResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantResourceServiceImpl implements RestaurantResourceService {
    @Value("${restaurant.resources.path}")
    private String restaurantResourcePath;
    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final MenuRepository menuRepository;
    private final ItemRepository itemRepository;
    private final TableRepository tableRepository;

    @Override
    public List<RestaurantDTO> getRestaurants(RestaurantFilterDTO filter) {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        if (filter != null && !filter.getCategoryIds().isEmpty()) {
            restaurants = restaurants.stream()
                    .filter(r -> filter.getCategoryIds().contains(r.getCategoryId()))
                    .toList();
        }

        if (filter != null && StringUtils.hasText(filter.getRestaurantName())) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getRestaurantName().toLowerCase().contains(filter.getRestaurantName().toLowerCase()))
                    .toList();
        }

        List<RestaurantDTO> restaurantDTOS = restaurants.stream()
                .map(r -> RestaurantDTO.builder()
                        .id(r.getId())
                        .restaurantName(r.getRestaurantName())
                        .logo(r.getLogo())
                        .build()
                ).toList();

        for (RestaurantDTO r : restaurantDTOS) {
            List<Location> locations = locationRepository.findByRestaurantId(r.getId());

            List<LocationDTO> locationDTOS = locations.stream()
                    .map(l -> LocationDTO.builder()
                            .id(l.getId())
                            .address(l.getAddress())
                            .build()
                    ).toList();
            r.setLocations(locationDTOS);
        }
        return restaurantDTOS;
    }

    @Override
    public MenuDTO getMenuByRestaurantId(Long restaurantId) {
        Optional<Menu> menu = menuRepository.findByRestaurantId(restaurantId);
        if (menu.isPresent()) {
            List<Item> items = itemRepository.findByMenuId(menu.get().getRestaurantId());
            List<ItemDTO> itemDTOS = items.stream()
                    .map(item -> ItemDTO.builder()
                            .id(item.getId())
                            .dishName(item.getDishName())
                            .price(item.getPrice())
                            .description(item.getDescription())
                            .image(item.getImage())
                            .isAvailable(item.getIsAvailable())
                            .build())
                    .toList();

            return MenuDTO.builder()
                    .id(menu.get().getId())
                    .items(itemDTOS)
                    .build();
        }
        throw new RestaurantResourceException("We encountered an error with the menu of the restaurant, please try again later.");
    }

    @Override
    public List<LocationDTO> getLocationsByRestaurantId(Long restaurantId) {
        List<Location> locations = locationRepository.findByRestaurantId(restaurantId);
        if (!locations.isEmpty()) {
            return locations.stream()
                    .map(location -> LocationDTO.builder()
                            .id(location.getId())
                            .address(location.getAddress())
                            .build()
                    ).toList();
        }
        throw new RestaurantResourceException("We encountered an error with the locations, please try again later.");
    }

    @Override
    public List<TableDTO> getFreeTablesByLocationId(Long locationId) {
        log.info("Get tables by location id {}", locationId);
        return tableRepository.getTablesByLocationId(locationId).stream()
                .filter(Table::getIsFree)
                .map(t -> TableDTO.builder()
                        .id(t.getId())
                        .locationId(t.getLocationId())
                        .isFree(t.getIsFree())
                        .build()
                ).toList();
    }

    @Override
    public Boolean isFreeTable(Long tableId) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new BookingException("Service currently unavailable, please try a little later!");
        }
        return table.get().getIsFree();
    }

    @Override
    public void updateTableFreeStatus(Long tableId, Boolean status) {
        tableRepository.updateTableFreeStatus(tableId, status);
    }

    @Override
    public Double calcAndGetPrice(List<Long> items) {
        List<Item> itemList = itemRepository.findAllById(items);

        AtomicReference<Double> price = new AtomicReference<>(0.0);

        for (Long i : items) {
            itemList.stream()
                    .filter(itm -> Objects.equals(itm.getId(), i))
                    .findFirst()
                    .ifPresent(item -> price.updateAndGet(v -> v + item.getPrice()));
        }

        return price.get();
    }

    @Override
    public String getItemsToText(List<Long> itemIds) {
        List<Item> items = itemRepository.findAllById(itemIds);

        // Grupăm după ID și numărăm aparițiile
        Map<Long, Long> itemCounts = itemIds.stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        // Convertim în text
        return items.stream()
                .distinct() // evităm dublurile în listă
                .map(item -> {
                    Long count = itemCounts.get(item.getId());
                    double totalPrice = item.getPrice() * count;
                    return item.getDishName() + " (x" + count + ") - " + totalPrice;
                })
                .collect(Collectors.joining(","));
    }

    @Override
    public void freeTable() {
        tableRepository.freeTable();
    }

    @Override
    public Optional<Restaurant> findRestaurantByLocationId(Long locationId) {
        return locationRepository.findById(locationId)
                .flatMap(value -> restaurantRepository.findById(value.getRestaurantId()));

    }

    private byte[] getImage(String name) {
        if (!StringUtils.hasText(name)) {
            return new byte[0];
        }

        try {
            Path path = new ClassPathResource("images/" + name).getFile().toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.error("Failed to read image {}, cause {}", name, e.getMessage());
            return new byte[0];
        }
    }

}