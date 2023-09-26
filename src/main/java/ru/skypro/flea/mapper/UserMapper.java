package ru.skypro.flea.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.skypro.flea.dto.UpdateUserDto;
import ru.skypro.flea.dto.UserDto;
import ru.skypro.flea.model.User;

@Mapper
public interface UserMapper {

    UserDto toUserDto(User entity);

    void updateUserFromDto(@MappingTarget User user, UpdateUserDto dto);

}
