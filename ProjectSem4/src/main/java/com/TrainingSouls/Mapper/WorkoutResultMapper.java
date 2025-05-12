package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Response.WorkoutResultDTO;
import com.TrainingSouls.Entity.WorkoutResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkoutResultMapper {
    WorkoutResultMapper INSTANCE = Mappers.getMapper(WorkoutResultMapper.class);

    @Mapping(source = "workoutPlan.exercise.name", target = "exerciseName")
    WorkoutResultDTO toDTO(WorkoutResult workoutResult);
}

