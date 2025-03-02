package com.example.web.mappers;

import com.example.domain.Profile.Profile;
import com.example.web.dto.profile.ProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO toDto(Profile profile);

    Profile toEntity(ProfileDTO profileDTO);
}
