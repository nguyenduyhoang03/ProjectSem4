package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Response.PostResponse;
import com.TrainingSouls.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.userID", target = "userID")
    @Mapping(source = "user.name", target = "name")
    PostResponse toPostResponse(Post post);
}
