package agin.designpatternproject.controller;

import agin.designpatternproject.entity.Item;
import agin.designpatternproject.proxy.ItemService;
import agin.designpatternproject.proxy.ItemServiceProxy;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceProxy itemService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("create")
    public ResponseEntity<Item> createItem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item, authHeader));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("delete")
    public ResponseEntity<String> deleteItem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Long itemId) {
        return ResponseEntity.ok(itemService.deleteItem(itemId, authHeader));
    }


}
