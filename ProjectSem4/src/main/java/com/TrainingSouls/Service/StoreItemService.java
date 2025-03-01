package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public StoreItem addStoreItem(StoreItem storeItem){
        return storeItemRepository.save(storeItem);
    }

}
