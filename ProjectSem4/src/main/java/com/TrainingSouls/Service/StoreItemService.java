package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.StoreItemReq;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreItemService {
    private final StoreItemRepository storeItemRepository;

    public List<StoreItem> getAll(){
        return storeItemRepository.findAll();
    }

    public StoreItem getById(int id){
        return storeItemRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
    }

    public StoreItem addStoreItem(StoreItemReq storeItemReq) {
        StoreItem storeItem = new StoreItem();
        storeItem.setName(storeItemReq.getName());
        storeItem.setPointsRequired(storeItemReq.getPointsRequired());
        storeItem.setQuantity(storeItemReq.getQuantity());
        storeItem.setDescription(storeItemReq.getDescription());

        return storeItemRepository.save(storeItem);
    }


    public StoreItem updateStoreItem(Integer id, StoreItemReq storeItemReq){
        StoreItem storeItem = storeItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // Cập nhật dữ liệu
        storeItem.setName(storeItemReq.getName());
        storeItem.setPointsRequired(storeItemReq.getPointsRequired());
        storeItem.setQuantity(storeItemReq.getQuantity());
        storeItem.setDescription(storeItemReq.getDescription());

        return storeItemRepository.save(storeItem);
    }


    public void deleteStoreItem(int id){
        storeItemRepository.deleteById(id);
    }

}
