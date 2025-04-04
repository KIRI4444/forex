package com.example.web.mappers.chat;

import com.example.domain.chat.Chat;
import com.example.web.dto.chat.ChatDTO;
import org.springframework.stereotype.Component;

@Component
public interface ChatMapper {

    ChatDTO toDTO(Chat chat, Long currentUserId);
}