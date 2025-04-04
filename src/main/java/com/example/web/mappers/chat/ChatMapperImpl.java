package com.example.web.mappers.chat;

import com.example.domain.Profile.Profile;
import com.example.domain.chat.Chat;
import com.example.web.dto.chat.ChatDTO;
import com.example.web.utils.Util;
import org.springframework.stereotype.Component;

@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatDTO toDTO(Chat chat, Long currentUserId) {
        if (chat == null) {
            return null;
        }

        Profile otherUser = chat.getFirstUser().getId() == currentUserId
                ? chat.getSecondUser().getProfile()
                : chat.getFirstUser().getProfile();

        // Формируем DTO
        return new ChatDTO(
                chat.getId(),
                otherUser.getName(),
                otherUser.getPhoto() != null ? otherUser.getPhoto().getPhoto() : null
        );
    }
}
