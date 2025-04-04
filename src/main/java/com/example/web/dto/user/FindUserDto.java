package com.example.web.dto.user;

import lombok.Data;

@Data
public class FindUserDto {
    private String name;
    private Long id;
    private String photo;

    public FindUserDto(String name, Long id, String photo) {
        this.name = name;
        this.id = id;
        this.photo = photo;
    }
}