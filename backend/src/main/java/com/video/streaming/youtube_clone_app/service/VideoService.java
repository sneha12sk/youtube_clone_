package com.video.streaming.youtube_clone_app.service;

import com.video.streaming.youtube_clone_app.dto.CommentDTO;
import com.video.streaming.youtube_clone_app.dto.UploadVideoResponse;
import com.video.streaming.youtube_clone_app.dto.VideoDto;
import com.video.streaming.youtube_clone_app.model.Comment;
import com.video.streaming.youtube_clone_app.model.Video;
import com.video.streaming.youtube_clone_app.repository.VideoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService
{
    private final S3Service videoById;
    private final VideoRepo videoRepo;
    private final UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile)
    {
        //Upload file to AWS S3
        // Save video data to Database
       String videoUrl= videoById.uploadFile(multipartFile);
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

       String thumbnailUrl= videoById.uploadFile(file);

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
        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId);
        return mapToVideoDTO(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo)
    {
        savedVideo.incrementViewCount();
        videoRepo.save(savedVideo);

    }

    public VideoDto likeVideo(String videoId)
    {
        Video videoById=getVideoById(videoId);

        if(userService.ifLikedVideo(videoId))               //the user liked the video and then unliked it
        {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        }
        else if (userService.ifDislikedVideo(videoId))      //the user disliked the video, changed their mind and liked the video again
        {
            videoById.decrementLikes();
            userService.removeFromDislikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        else {
            videoById.incrementLikes();  //Also need to add this video to history of users
            userService.addToLikedVideos(videoId);

        }
        videoRepo.save(videoById);

        return mapToVideoDTO(videoById);



    }

    public VideoDto dislikeVideo(String videoId) {

        Video videoById=getVideoById(videoId);

        if(userService.ifDislikedVideo(videoId))               //the user liked the video and then unliked it
        {
            videoById.decrementdislikes();
            userService.removeFromDislikedVideos(videoId);
        }
        else if (userService.ifLikedVideo(videoId))      //the user disliked the video, changed their mind and liked the video again
        {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            videoById.incrementdislikes();
            userService.addToDislikedVideos(videoId);
        }
        else {
            videoById.incrementdislikes();
            userService.addToDislikedVideos(videoId);

        }
        videoRepo.save(videoById);

        return mapToVideoDTO(videoById);



    }

    private static VideoDto mapToVideoDTO(Video videoById) {
        VideoDto videoDto= new VideoDto();

        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDislikes().get());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDislikes().get());
        videoDto.setViewCount(videoById.getViewCount().get());
        return videoDto;
    }

    public void addComment(String videoId, CommentDTO commentDTO)
    {
        Video video=getVideoById(videoId);
        Comment comment=new Comment();
        comment.setText(commentDTO.getCommentText());
        comment.setAuthorId(commentDTO.getAuthorId());
        video.addComment(comment);
        videoRepo.save(video);
    }

    public List<CommentDTO> getAllComments(String videoId) {
        Video video= getVideoById(videoId);
        List<Comment> commentList=video.getCommentList();
        return commentList.stream().map(this::mapToCommentDTO).toList();
    }

    private CommentDTO mapToCommentDTO(Comment comment)
    {
        CommentDTO commentDTO= new CommentDTO();
        commentDTO.setCommentText(comment.getText());
        commentDTO.setAuthorId(comment.getAuthorId());
        return  commentDTO;
    }

    public List<VideoDto> getAllVideos() {
        return videoRepo.findAll().stream().map(VideoService::mapToVideoDTO).toList();
    }
}
