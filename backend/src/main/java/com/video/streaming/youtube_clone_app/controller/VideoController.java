package com.video.streaming.youtube_clone_app.controller;

import com.video.streaming.youtube_clone_app.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")

public class VideoController
{


    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadVideo(@RequestParam("file") MultipartFile file)
    {
       videoService.uploadVideo(file);
    }
}
