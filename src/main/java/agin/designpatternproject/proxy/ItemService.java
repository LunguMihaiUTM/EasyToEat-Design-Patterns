package agin.designpatternproject.proxy;

import agin.designpatternproject.entity.Item;

public interface ItemService {
    Item createItem(Item item, String jwtToken);
    String deleteItem(Long itemId, String jwtToken);
}
