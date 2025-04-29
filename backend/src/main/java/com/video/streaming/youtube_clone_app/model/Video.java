package com.video.streaming.youtube_clone_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value = "Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video
{
    @Id
    private String id;
    private String title;
    private String description;
    private  String userId;
    private AtomicInteger likes= new AtomicInteger(0);
    private  AtomicInteger dislikes= new AtomicInteger(0);
    private Set<String>tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private AtomicInteger viewCount= new AtomicInteger(0);;
    private String thumbnailUrl;
    private List<Comment>commentList=new CopyOnWriteArrayList<>();

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public void incrementLikes()
    {
        likes.incrementAndGet();
    }

    public void decrementLikes() {
        if (likes.get() > 0) {
            likes.decrementAndGet();
        }
    }

    public void decrementdislikes() {
        if (dislikes.get() > 0) {
            dislikes.decrementAndGet();
        }
    }

    public void incrementdislikes()
    {
        dislikes.incrementAndGet();
    }


    public void incrementViewCount() {
        viewCount.incrementAndGet();
    }

    public void addComment(Comment comment)
    {
        commentList.add(comment);
    }
}
