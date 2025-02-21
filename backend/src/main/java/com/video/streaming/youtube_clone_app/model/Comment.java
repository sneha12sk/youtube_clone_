package com.video.streaming.youtube_clone_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment
{
    @Id
    private String id;
    private String text;
    private String authorId;
    private Integer like;
    private Integer dislike;
}
