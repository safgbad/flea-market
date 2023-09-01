package ru.skypro.flea.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.NewPasswordDto;
import ru.skypro.flea.dto.UpdateUserDto;
import ru.skypro.flea.dto.UserDto;
import ru.skypro.flea.exception.FileSystemError;
import ru.skypro.flea.exception.UserNotFoundError;
import ru.skypro.flea.mapper.UserMapper;
import ru.skypro.flea.model.User;
import ru.skypro.flea.model.enums.Role;
import ru.skypro.flea.repository.UserRepository;
import ru.skypro.flea.service.ImageService;
import ru.skypro.flea.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder encoder;

    private final ImageService imageService;

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            String message = String.format("User %s has not been found", email);
            log.error(message);
            throw new UserNotFoundError(message);
        }
        return userOptional.get();
    }

    @Override
    public boolean isAuthorizedUserAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(Role.ADMIN.name()));
    }

    @Override
    public void setPassword(NewPasswordDto newPasswordDto,
                            Authentication authentication) {
        String email = authentication.getName();
        User user = getUserByEmail(email);
        user.setPassword(encoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto getUser(Authentication authentication) {
        User user = getUserByEmail(authentication.getName());

        return userMapper.toUserDto(user);
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUser,
                                    Authentication authentication) {
        String email = authentication.getName();
        User user = getUserByEmail(email);
        userMapper.updateUserFromDto(user, updateUser);
        userRepository.save(user);

        return updateUser;
    }

    @Override
    public void updateUserImage(MultipartFile image,
                                Authentication authentication) {
        String email = authentication.getName();
        User user = getUserByEmail(email);
        try {
            byte[] bytes = image.getBytes();
            String imageName = imageService.saveImage(image, "user-" + user.getId());
            user.setImage("/img/" + imageName);
            userRepository.save(user);
        } catch (IOException e) {
            String message = String.format("Unable to save image for user %s", email);
            log.info(message);
            throw new FileSystemError(message);
        }
    }

}
