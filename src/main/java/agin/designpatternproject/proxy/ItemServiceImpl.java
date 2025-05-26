package agin.designpatternproject.proxy;

import agin.designpatternproject.entity.Item;
import agin.designpatternproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item createItem(Item item, String jwtToken) {
        itemRepository.save(item);
        return item;
    }

    @Override
    public String deleteItem(Long itemId, String jwtToken) {
        itemRepository.deleteById(itemId);
        return "Item deleted successfully.";
    }
}
