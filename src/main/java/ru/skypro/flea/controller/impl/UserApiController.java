package ru.skypro.flea.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.controller.UserApi;
import ru.skypro.flea.dto.NewPasswordDto;
import ru.skypro.flea.dto.UpdateUserDto;
import ru.skypro.flea.dto.UserDto;
import ru.skypro.flea.service.UserService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserApiController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> setPassword(NewPasswordDto newPassword,
                                            Authentication authentication) {
        userService.setPassword(newPassword, authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        UserDto user = userService.getUser(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @Override
    public ResponseEntity<UpdateUserDto> updateUser(UpdateUserDto updateUser,
                                                    Authentication authentication) {
        UpdateUserDto user = userService.updateUser(updateUser, authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @Override
    public ResponseEntity<Void> updateUserImage(MultipartFile image,
                                                Authentication authentication) {
        userService.updateUserImage(image, authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
