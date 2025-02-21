package com.video.streaming.youtube_clone_app.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService
{

    String uploadFile(MultipartFile file);
}
