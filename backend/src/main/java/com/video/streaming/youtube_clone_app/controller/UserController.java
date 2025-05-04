package com.video.streaming.youtube_clone_app.controller;

import com.video.streaming.youtube_clone_app.service.UserRegisterService;
import com.video.streaming.youtube_clone_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController
{
    private final UserRegisterService userRegisterService;
    private final UserService userService;


    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String register(Authentication authentication)
    {
        Jwt jwt=(Jwt)authentication.getPrincipal();
       return userRegisterService.registerUser(jwt.getTokenValue());

    }

    @PostMapping("subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribedUser(@PathVariable String userId)
    {
        userService.subscribeUser(userId);
        return true;
    }

    @PostMapping("unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unsubscribedUser(@PathVariable String userId)
    {
        userService.unsubscribeUser(userId);
        return true;
    }

    //User History API
    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory (@PathVariable String userId)
    {
        return userService.getUserHistory(userId);
    }
}
