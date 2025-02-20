package com.TrainingSouls.Repository;

import com.TrainingSouls.DTO.Response.PostResponse;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

}
