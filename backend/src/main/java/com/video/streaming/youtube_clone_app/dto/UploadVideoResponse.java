package com.video.streaming.youtube_clone_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoResponse
{
    private String videoId;
    private String videoUrl;

}
