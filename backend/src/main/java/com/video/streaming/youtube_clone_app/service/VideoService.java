package com.video.streaming.youtube_clone_app.service;

import com.video.streaming.youtube_clone_app.dto.UploadVideoResponse;
import com.video.streaming.youtube_clone_app.dto.VideoDto;
import com.video.streaming.youtube_clone_app.model.Video;
import com.video.streaming.youtube_clone_app.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service

public class VideoService
{
    private final S3Service s3Service;
    private final VideoRepo videoRepo;

    @Autowired
    public VideoService(S3Service s3Service, VideoRepo videoRepo) {
        this.s3Service = s3Service;
        this.videoRepo = videoRepo;
    }

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile)
    {
        //Upload file to AWS S3
        // Save video data to Database
       String videoUrl= s3Service.uploadFile(multipartFile);
       var video=new Video();
       video.setVideoUrl(videoUrl);

       var savedVideo=videoRepo.save(video);
       return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto)
    {
        //find the video by the video ID
        var savedVideo=getVideoById(videoDto.getId());
        //Map the videoDto fields to video

        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());

        //Save the video to the database
        videoRepo.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId)
    {
        var savedVideo=getVideoById(videoId);

       String thumbnailUrl= s3Service.uploadFile(file);

       savedVideo.setThumbnailUrl(thumbnailUrl);
       videoRepo.save(savedVideo);

      return thumbnailUrl;

    }

    Video getVideoById(String videoId)
    {
        return videoRepo.findById(videoId)
                .orElseThrow(()->new IllegalArgumentException("Cannot find video by the ID- "+videoId));
    }

    public VideoDto getVideoDetails(String videoId)
    {
        Video savedVideo=getVideoById(videoId);

        VideoDto videoDto= new VideoDto();

        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());

        return videoDto;
    }
}
