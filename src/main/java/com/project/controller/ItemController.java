//controller/ItemController
package com.project.controller;

import com.project.entity.Item;
import com.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/addItem")
    public Item addItem(@RequestParam String name, @RequestParam double price) {
        return itemService.addItem(name, price);
    }

    @GetMapping("/add")
    public String home() {
        return "AAA";
    }
    @GetMapping("/getItem")
    public List<Item> getItems() {
        return itemService.getAllItems();
    }
}