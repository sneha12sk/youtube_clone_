package com.video.streaming.youtube_clone_app.dto;


import com.video.streaming.youtube_clone_app.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto
{
    private String id;
    private String title;
    private String description;
    private String videoUrl;
    private Set<String> tags;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
}
