package com.video.streaming.youtube_clone_app.repository;

import com.video.streaming.youtube_clone_app.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepo extends MongoRepository<Video,String>
{
}
