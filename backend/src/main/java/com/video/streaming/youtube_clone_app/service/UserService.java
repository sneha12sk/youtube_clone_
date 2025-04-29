package com.video.streaming.youtube_clone_app.service;

import com.video.streaming.youtube_clone_app.model.User;
import com.video.streaming.youtube_clone_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User getCurrentUser()
    {
        String sub= ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("sub");

        return userRepo.findBySub(sub)
                .orElseThrow(() -> new NoSuchElementException("User not found for sub: " + sub));
    }

    public void addToLikedVideos(String videoId)
    {
        User currentUser= getCurrentUser();
        currentUser.addToLikedVideos(videoId);
        userRepo.save(currentUser);
    }
    public boolean ifLikedVideo(String videoId)         //to check if the video is already liked by the user
    {
           return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo->likedVideo.equals(videoId));
    }

    public boolean ifDislikedVideo(String videoId)         //to check if the video is already liked by the user
    {
        return getCurrentUser().getDislikedVideos().stream().anyMatch(likedVideo->likedVideo.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId)
    {
        User currentUser= getCurrentUser();
        currentUser.removeFromLikedVideos(videoId);
        userRepo.save(currentUser);
    }

    public void removeFromDislikedVideos(String videoId) {
        User currentUser= getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepo.save(currentUser);
    }

    public void addToDislikedVideos(String videoId)
    {
        User currentUser= getCurrentUser();
        currentUser.addToDislikedVideos(videoId);
        userRepo.save(currentUser);
    }

    public void addVideoToHistory(String videoId) {
        User currentUser=getCurrentUser();
        currentUser.addToVideoHistory(videoId);
        userRepo.save(currentUser);
    }

    public void subscribeUser(String userId)
    {
        User currentUser=getCurrentUser();
        currentUser.addToSubscribedToUser(userId);
        User user = getUserById(userId);
        user.addToSubscribers(currentUser.getId());

       userRepo.save(currentUser);
       userRepo.save(user);
    }

    public void unsubscribeUser(String userId)
    {
        User currentUser=getCurrentUser();
        currentUser.removeFromSubscribedToUser(userId);
        User user = getUserById(userId);
        user.removeFromSubscribers(currentUser.getId());

        userRepo.save(currentUser);
        userRepo.save(user);
    }

    public Set<String> getUserHistory(String userId) {
        User user=getUserById(userId);
        return user.getVideoHistory();

    }

    private User getUserById(String userId) {
        User user= userRepo.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found"+ userId));
        return user;
    }
}
