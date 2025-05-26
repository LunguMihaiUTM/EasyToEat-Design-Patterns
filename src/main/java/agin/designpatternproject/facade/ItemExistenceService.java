package agin.designpatternproject.facade;

import agin.designpatternproject.entity.Item;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemExistenceService {

    private final ItemRepository itemRepository;

    public boolean verifyItems(List<Long> itemIds) {
        List<Item> items = itemRepository.findAllById(itemIds);
        if (items.isEmpty()) {
            throw new RestaurantResourceException("One or more items do not exist.");
        }
        return true;
    }
}