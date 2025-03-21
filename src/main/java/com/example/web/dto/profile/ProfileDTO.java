package com.example.web.dto.profile;

import com.example.web.dto.validation.OnCreate;
import com.example.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProfileDTO {
    @JsonProperty(required = false)
    private long id;

    @Length(min = 1, max = 255, message = "Name length must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    private int age;

    private String description;

    private Integer sex;

    @JsonProperty(required = false)
    private String photo;
}
