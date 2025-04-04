package com.example.web.mappers.message;

import com.example.domain.Message.Message;
import com.example.domain.chat.Chat;
import com.example.domain.user.User;
import com.example.web.dto.message.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public interface MessageMapper {
    MessageDTO toDTO(Message message);
    Message toEntity(MessageDTO dto, Chat chat, User sender);
}
