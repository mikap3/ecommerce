package com.udacity.jwdnd.course4.ecommerce.service;

import com.udacity.jwdnd.course4.ecommerce.entity.Item;
import com.udacity.jwdnd.course4.ecommerce.exception.ItemNotFoundException;
import com.udacity.jwdnd.course4.ecommerce.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Item findItemById(Long itemId) throws ItemNotFoundException {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    public List<Item> findItemsByName(String name) {
        return itemRepository.findByName(name);
    }
}
