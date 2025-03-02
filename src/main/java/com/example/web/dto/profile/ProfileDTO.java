package com.example.web.dto.profile;

import com.example.web.dto.validation.OnCreate;
import com.example.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProfileDTO {

    private long id;

    @NotNull(message = "name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 1, max = 255, message = "Name length must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Age must be not null", groups = {OnCreate.class, OnUpdate.class})
    private int age;

    private String description;

    @NotNull(message = "Sex must be not null", groups = {OnCreate.class, OnUpdate.class})
    private int sex;
}
