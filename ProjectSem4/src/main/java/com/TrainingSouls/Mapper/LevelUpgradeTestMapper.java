package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Request.CreateLevelUpgradeTest;
import com.TrainingSouls.Entity.LevelUpgradeTest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LevelUpgradeTestMapper {
    LevelUpgradeTestMapper INSTANCE = Mappers.getMapper(LevelUpgradeTestMapper.class);

    LevelUpgradeTest toEntity(CreateLevelUpgradeTest dto);

    CreateLevelUpgradeTest toDTO(LevelUpgradeTest entity);
}
