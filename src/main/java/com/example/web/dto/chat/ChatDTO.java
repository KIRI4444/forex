package com.example.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ChatDTO {
    private Long id;
    private String name;
    private String photo;

    public ChatDTO(Long id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}
