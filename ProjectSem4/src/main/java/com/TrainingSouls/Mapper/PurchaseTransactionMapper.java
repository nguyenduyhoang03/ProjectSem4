package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Request.PurchaseTransactionResponse;
import com.TrainingSouls.DTO.Response.StoreItemDTO;
import com.TrainingSouls.DTO.Response.UserDTO;
import com.TrainingSouls.Entity.PurchaseTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  PurchaseTransactionMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "item", target = "item")
    PurchaseTransactionResponse toDto(PurchaseTransaction purchaseTransaction);

    @Mapping(source = "user.userID", target = "user.userID")
    UserDTO toDto(User user);

    @Mapping(source = "name", target = "name")
    StoreItemDTO toDto(StoreItem storeItem);

}

