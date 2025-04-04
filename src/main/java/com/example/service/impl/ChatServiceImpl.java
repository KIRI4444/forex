package com.example.service.impl;

import com.example.domain.chat.Chat;
import com.example.domain.response.ApiResponse;
import com.example.domain.user.User;
import com.example.repository.ChatRepository;
import com.example.repository.UserRepository;
import com.example.service.ChatService;
import com.example.web.dto.chat.ChatDTO;
import com.example.web.mappers.chat.ChatMapper;
import com.example.web.utils.Util;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Override
    public ApiResponse<?> getUserChats(int page, int size) {
        Long userId = Util.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chatPage = chatRepository.findUserChats(userId, pageable);

        chatPage.forEach(chat -> {
            Hibernate.initialize(chat.getFirstUser());
            Hibernate.initialize(chat.getSecondUser());

            Hibernate.initialize(chat.getFirstUser().getProfile());
            Hibernate.initialize(chat.getSecondUser().getProfile());
        });

        List<ChatDTO> result = chatPage.stream()
                .map(chat -> chatMapper.toDTO(chat, userId))
                .toList();

        return new ApiResponse<>("Chats found", result);
    }

    @Override
    public ApiResponse<?> createChat(Long secondUserId) {
        Long firstUserId = Util.getCurrentUserId();
        User user1 = userRepository.getUserById(firstUserId).orElse(null);
        User user2 = userRepository.getUserById(secondUserId).orElse(null);

        if (user1 == null || user2 == null) {
            return new ApiResponse<>("Error creating chat", null);
        }

        if (firstUserId > secondUserId) {
            User temp = user1;
            user1 = user2;
            user2 = temp;
        }

        Optional<Chat> existingChat = findChatBetweenUsers(user1, user2);

        if (existingChat.isPresent()) {
            ChatDTO chatDTO = chatMapper.toDTO(existingChat.get(), firstUserId);
            return new ApiResponse<>("Chat already exists", chatDTO);
        }

        Chat chat = createChatBetweenTwoUsers(user1, user2);
        ChatDTO chatDTO = chatMapper.toDTO(chat, firstUserId);

        return new ApiResponse<>("Chat created", chatDTO);
    }

    @Override
    public Chat createChatBetweenTwoUsers(User user1, User user2) {
        Chat chat = new Chat();
        chat.setFirstUser(user1);
        chat.setSecondUser(user2);

        chat = chatRepository.save(chat);

        user1.getChatsAsFirstUser().add(chat);
        user2.getChatsAsSecondUser().add(chat);

        userRepository.save(user1);
        userRepository.save(user2);

        return chat;
    }

    @Override
    public Optional<Chat> findChatBetweenUsers(User user1, User user2) {
        Long userId1 = user1.getId();
        Long userId2 = user2.getId();
        return chatRepository.findChatBetweenUsers(userId1, userId2);
    }
}
