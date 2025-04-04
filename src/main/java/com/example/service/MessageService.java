package com.example.service;

import com.example.domain.Message.Message;
import com.example.domain.chat.Chat;
import com.example.domain.response.ApiResponse;
import com.example.domain.user.User;
import com.example.web.dto.message.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    Message saveMessage(MessageDTO message);
    ApiResponse<?> getMessagesFromChat(Long chatId, int page, int size);
}
