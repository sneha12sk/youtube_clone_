package com.video.streaming.youtube_clone_app.controller;

import com.video.streaming.youtube_clone_app.dto.UploadVideoResponse;
import com.video.streaming.youtube_clone_app.dto.VideoDto;
import com.video.streaming.youtube_clone_app.model.Video;
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
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file)

    {
       return videoService.uploadVideo(file);
    }

    //Implement Upload Thumbnail API
    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId")  String videoId)

    {
       return videoService.uploadThumbnail(file,videoId);
    }

    @PutMapping
            @ResponseStatus(HttpStatus.OK)
    public  VideoDto editVideoMetadata(@RequestBody VideoDto videoDto)
    {
       return videoService.editVideo(videoDto);
    }



    //get video details method
    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId)
    {
       return  videoService.getVideoDetails(videoId);
    }
}

