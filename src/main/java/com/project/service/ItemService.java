package com.project.service;

import com.project.entity.Item;
import com.project.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(String name, double price) {
        Item newItem = new Item(name, price);
        return itemRepository.save(newItem);
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}