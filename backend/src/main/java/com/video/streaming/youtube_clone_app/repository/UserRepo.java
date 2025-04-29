package com.video.streaming.youtube_clone_app.repository;

import com.video.streaming.youtube_clone_app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    Optional<User> findBySub(String sub);
}
