package agin.designpatternproject.facade;

import agin.designpatternproject.entity.Item;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {

    private final ItemRepository itemRepository;

    public double calculateTotalPrice(List<Long> itemIds) {
        List<Item> allItems = itemRepository.findAllById(itemIds);

        Map<Long, Item> itemMap = allItems.stream()
                .collect(Collectors.toMap(Item::getId, item -> item, (a, b) -> a));

        return itemIds.stream()
                .mapToDouble(id -> {
                    Item item = itemMap.get(id);
                    if (item == null) {
                        throw new RestaurantResourceException("Item with ID " + id + " not found.");
                    }
                    return item.getPrice();
                })
                .sum();
    }
}
