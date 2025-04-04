package com.example.service.impl;

import com.example.domain.Message.Message;
import com.example.domain.chat.Chat;
import com.example.domain.exception.AccessDeniedException;
import com.example.domain.response.ApiResponse;
import com.example.domain.user.User;
import com.example.repository.ChatRepository;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import com.example.service.MessageService;
import com.example.web.dto.message.MessageDTO;
import com.example.web.mappers.message.MessageMapper;
import com.example.web.utils.Util;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;


    @Override
    public Message saveMessage(MessageDTO messageDTO) {
        Chat chat = chatRepository.findById(messageDTO.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        if (!isUserInChat(chat)) {
            throw new AccessDeniedException();
        }

        User sender = userRepository.findById(Util.getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = messageMapper.toEntity(messageDTO, chat, sender);

        return messageRepository.save(message);
    }

    @Override
    public ApiResponse<?> getMessagesFromChat(Long chatId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        if (!isUserInChat(chat)) {
            throw new AccessDeniedException();
        }

        Page<Message> messagePage = messageRepository.findByIdOrderByCreatedAtDesc(chatId, pageable);

        messagePage.forEach(message -> Hibernate.initialize(message.getSender().getProfile()));

        List<MessageDTO> messageDTOs = messagePage.getContent().stream()
                .map(messageMapper::toDTO)
                .toList();


        return new ApiResponse<>("Messages found", messageDTOs);
    }

    private boolean isUserInChat(Chat chat) {
        Long currentUserId = Util.getCurrentUserId();
        return chat.getFirstUser().getId() == currentUserId || chat.getSecondUser().getId() == currentUserId;
    }
}
