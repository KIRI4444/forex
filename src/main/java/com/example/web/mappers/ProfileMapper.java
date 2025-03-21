package com.example.web.mappers;

import com.example.domain.Profile.Profile;
import com.example.web.dto.profile.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(target = "photo", source = "profile.photo.photo")
    ProfileDTO toDto(Profile profile);

    @Mapping(target = "photo.photo", source = "profileDTO.photo")
    Profile toEntity(ProfileDTO profileDTO);
}
