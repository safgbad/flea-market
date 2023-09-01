package ru.skypro.flea.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.NewPasswordDto;
import ru.skypro.flea.dto.UpdateUserDto;
import ru.skypro.flea.dto.UserDto;
import ru.skypro.flea.model.User;

public interface UserService {
    User getUserByEmail(String email);

    boolean isAuthorizedUserAdmin(Authentication authentication);

    void setPassword(NewPasswordDto newPasswordDto,
                     Authentication authentication);

    UserDto getUser(Authentication authentication);

    UpdateUserDto updateUser(UpdateUserDto updateUser,
                             Authentication authentication);

    void updateUserImage(MultipartFile image,
                         Authentication authentication);
}
