package com.ead.authuser.mapper;

import com.ead.authuser.models.dto.UserDto;
import com.ead.authuser.models.entity.UserModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "userStatus", ignore = true)
    UserModel toEntity(UserDto userDto);

    UserDto toDto(UserModel userModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UserModel updatedUser, @MappingTarget UserModel userModel);

}
