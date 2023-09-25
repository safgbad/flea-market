package ru.skypro.flea.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.flea.dto.UpdateUserDto;
import ru.skypro.flea.dto.UserDto;
import ru.skypro.flea.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private static User defaultUser;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @BeforeAll
    public static void fillDefaultUser() {
        defaultUser = new User();
        defaultUser.setId(123);
        defaultUser.setFirstName("John");
        defaultUser.setLastName("Brown");
        defaultUser.setEmail("john.brown2007@example.com");
        defaultUser.setPhone("+7(000)000-00-00");
        defaultUser.setImage("https://imagehostingservice.org/jnn41kr.png");
    }

    @Test
    public void userToUserDtoMappingTest() {
        UserDto dto = mapper.toUserDto(defaultUser);

        assertEntityEqualsToDto(defaultUser, dto);
    }

    @Test
    public void updatingUserFromUpdateUserDtoTest() {
        User entity = new User();
        entity.setFirstName("Old firstName");
        entity.setLastName("Old lastName");
        entity.setPhone("+7(111)111-11-11");

        UpdateUserDto dto = new UpdateUserDto();
        dto.setFirstName("New firstName");
        dto.setLastName("New lastName");
        dto.setPhone("+7(222)222-22-22");

        mapper.updateUserFromDto(entity, dto);

        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getPhone(), dto.getPhone());
    }

    private void assertEntityEqualsToDto(User entity, UserDto dto) {
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getPhone(), entity.getPhone());
        assertEquals(dto.getImage(), entity.getImage());
    }

}
