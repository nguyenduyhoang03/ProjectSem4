package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Request.UserProfileDTO;
import com.TrainingSouls.Entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toDto(UserProfile userProfile);
}

