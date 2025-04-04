package com.example.service;

import com.example.domain.chat.Chat;
import com.example.domain.response.ApiResponse;
import com.example.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ChatService {
    ApiResponse<?> getUserChats(int page, int size);
    ApiResponse<?> createChat(Long secondUserId);
    Chat createChatBetweenTwoUsers(User user1, User user2);
    Optional<Chat> findChatBetweenUsers(User firstUser, User secondUser);
}
