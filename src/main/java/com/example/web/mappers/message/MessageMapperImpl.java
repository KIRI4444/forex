package com.example.web.mappers.message;

import com.example.domain.Message.Message;
import com.example.domain.Profile.Profile;
import com.example.domain.chat.Chat;
import com.example.domain.user.User;
import com.example.web.dto.message.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDTO toDTO(Message message) {
        if (message == null) {
            return null;
        }

        String senderName = message.getSender().getProfile() != null
                ? message.getSender().getProfile().getName()
                : "Unknown";

        return new MessageDTO(
                message.getChat().getId(),
                message.getMessageText(),
                senderName,
                message.getCreatedAt()
        );
    }

    @Override
    public Message toEntity(MessageDTO dto, Chat chat, User sender) {
        if (dto == null) {
            return null;
        }

        Message message = new Message();
        message.setChat(chat);
        message.setMessageText(dto.getMessageText());
        message.setSender(sender);

        return message;
    }
}
