package com.video.streaming.youtube_clone_app.service;

import com.video.streaming.youtube_clone_app.model.Video;
import com.video.streaming.youtube_clone_app.repository.VideoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService
{
    private final S3Service s3Service;
    private final VideoRepo videoRepo;
    public void uploadVideo(MultipartFile multipartFile)
    {
        //Upload file to AWS S3
        // Save video data to Database
       String videoUrl= s3Service.uploadFile(multipartFile);
       var video=new Video();
       video.setVideoUrl(videoUrl);

       videoRepo.save(video);
    }

}
