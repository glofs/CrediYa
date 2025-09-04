package co.com.pragma.api;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.model.users.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper(); // Clase que contiene dtoToModel

    @Test
    void dtoToModel_ShouldMapAllFieldsCorrectly() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setBirthDay(LocalDate.of(1990, 1, 1));
        dto.setEmail("juan.perez@example.com");
        dto.setPay(143000);
        dto.setTelephone("123456789");
        dto.setAddress("Calle Falsa 123");

        // Act
        User model = userMapper.dtoToModel(dto);
        Data data = userMapper.userToResponse(model);

        // Assert
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getLastName(), model.getLastName());
        assertEquals(dto.getBirthDay(), model.getBirthDay());
        assertEquals(dto.getEmail(), model.getEmail());
        assertEquals(dto.getPay(), model.getPay());
        assertEquals(dto.getTelephone(), model.getTelephone());
        assertEquals(dto.getAddress(), model.getAddress());

        assertEquals(model.getId(), data.getData().getId());
        assertEquals(model.getName(), data.getData().getName());
        assertEquals(model.getLastName(), data.getData().getLastName());
        assertEquals(model.getBirthDay(), data.getData().getBirthDay());
        assertEquals(model.getEmail(), data.getData().getEmail());
        assertEquals(model.getPay(), data.getData().getPay());
        assertEquals(model.getTelephone(), data.getData().getTelephone());
        assertEquals(model.getAddress(), data.getData().getAddress());

    }
}