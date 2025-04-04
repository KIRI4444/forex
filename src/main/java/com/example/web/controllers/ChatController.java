package com.example.web.controllers;

import com.example.domain.response.ApiResponse;
import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("")
    public ApiResponse<?> getUserChats(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return chatService.getUserChats(page, size);
    }

    @PostMapping("/create")
    public ApiResponse<?> createChat(@RequestParam Long secondUserId) {
        return chatService.createChat(secondUserId);
    }
}
