package com.video.streaming.youtube_clone_app.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.streaming.youtube_clone_app.dto.UserInfoDTO;
import com.video.streaming.youtube_clone_app.model.User;
import com.video.streaming.youtube_clone_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    @Value("${auth0.userInfoEndPoint}")
    private String userInfoEndPoint;
    private final UserRepo userRepo;
    public String registerUser(String tokenValue)
    {
          // Make a call to user info endpoint
        HttpRequest httpRequest= (HttpRequest) HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndPoint))
                .setHeader("Authorization",String.format("Bearer %s",tokenValue))
                .build();

        try {
            HttpResponse<String> responseString= HttpClient.newHttpClient().send(httpRequest,HttpResponse.BodyHandlers.ofString());
            String body= responseString.body();
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            UserInfoDTO userInfoDTO=objectMapper.readValue(body,UserInfoDTO.class);

            Optional<User>userBySubject= userRepo.findBySub(userInfoDTO.getSub());
            if(userBySubject.isPresent())
            {
                return userBySubject.get().getId();
            }
            else{
                User user=new User();
                user.setFirstName(userInfoDTO.getGivenName());
                user.setLastName(userInfoDTO.getFamilyName());
                user.setFullName(userInfoDTO.getName());
                user.setEmail(userInfoDTO.getEmail());
                user.setSub(userInfoDTO.getSub());

                return userRepo.save(user).getId();

            }


        } catch (Exception e) {
            throw new RuntimeException(" Exception occurred while registering the user",e);
        }


    }
}
