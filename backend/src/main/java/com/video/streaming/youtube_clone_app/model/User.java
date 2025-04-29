package com.video.streaming.youtube_clone_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String sub;
    private Set<String> subscribedToUsers=ConcurrentHashMap.newKeySet();         //stores info about all the channel this user is subscribed to
    private Set<String> subscribers=ConcurrentHashMap.newKeySet();                //stores the info about all the subscribers subscribed to our channel
    private Set<String> videoHistory=ConcurrentHashMap.newKeySet();
    private Set<String>likedVideos= ConcurrentHashMap.newKeySet();
    private Set<String>dislikedVideos= ConcurrentHashMap.newKeySet();

    public void addToLikedVideos(String videoId)
    {
        likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId)
    {
        likedVideos.remove(videoId);
    }

    public void removeFromDislikedVideos(String videoId) {
        dislikedVideos.remove(videoId);
    }

    public void addToDislikedVideos(String videoId) {
        dislikedVideos.add(videoId);
    }

    public void addToVideoHistory(String videoId)
    {
        videoHistory.add(videoId);
    }

    public void addToSubscribedToUser(String userId) {
        subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String userId) {
        subscribers.add(userId);
    }

    public void removeFromSubscribedToUser(String userId) {
        subscribedToUsers.remove(userId);
    }

    public void removeFromSubscribers(String userId) {
        subscribers.remove(userId);
    }
}
