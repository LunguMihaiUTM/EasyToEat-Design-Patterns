package agin.designpatternproject.proxy;

import agin.designpatternproject.entity.Item;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.enums.Role;
import agin.designpatternproject.exception.UserException;
import agin.designpatternproject.util.TokenExtractor;
import agin.designpatternproject.util.UserExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceProxy implements ItemService {

    private final ItemServiceImpl itemServiceImpl;
    private final UserExtractor userExtractor;
    private final TokenExtractor tokenExtractor;

    @Override
    public Item createItem(Item item, String jwtToken) {
        String token = tokenExtractor.getToken(jwtToken); // elimină "Bearer "
        User user = userExtractor.getUser(token);
        if (user.getRole() != Role.ADMIN) {
            throw new UserException("Access denied: Only admins can create items.");
        }
        return itemServiceImpl.createItem(item, token);
    }

    @Override
    public String deleteItem(Long itemId, String jwtToken) {
        String token = tokenExtractor.getToken(jwtToken);
        User user = userExtractor.getUser(token);
        if (user.getRole() != Role.ADMIN) {
            throw new UserException("Access denied: Only admins can delete items.");
        }
        return itemServiceImpl.deleteItem(itemId, token);
    }
}