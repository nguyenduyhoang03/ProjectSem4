package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.StoreItemReq;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Service.StoreItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class StoreItemController {
    private final StoreItemService storeItemService;

    @GetMapping()
    public List<StoreItem> getAll(){
        return storeItemService.getAll();
    }

    @GetMapping("/{id}")
    public StoreItem getById(@PathVariable int id){
        return storeItemService.getById(id);
    }

    @PostMapping("/create-item")
    public StoreItem addStoreItem(@Valid @RequestBody StoreItemReq req){
        return storeItemService.addStoreItem(req);
    }

    @PutMapping("/update-item/{id}")
    public StoreItem updateStoreItem(@PathVariable Integer id, @Valid @RequestBody StoreItemReq req){
        return storeItemService.updateStoreItem(id, req);
    }


    @DeleteMapping("/{id}")
    public String deleteStoreItem(@PathVariable Integer id){
        storeItemService.deleteStoreItem(id);
        return "deleted";
    }
}
