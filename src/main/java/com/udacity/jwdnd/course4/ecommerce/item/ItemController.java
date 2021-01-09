package com.udacity.jwdnd.course4.ecommerce.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> listItems() {
        List<Item> items = itemService.findAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findItemById(@PathVariable Long id) {
        Item item = itemService.findItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> findItemsByName(@PathVariable String name) {
        List<Item> items = itemService.findItemsByName(name);
        if (items == null || items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
