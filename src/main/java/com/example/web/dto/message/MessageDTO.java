package com.example.web.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO {
    private Long chatId;
    private String messageText;
    private String senderName;
    private LocalDateTime createdAt;
}
