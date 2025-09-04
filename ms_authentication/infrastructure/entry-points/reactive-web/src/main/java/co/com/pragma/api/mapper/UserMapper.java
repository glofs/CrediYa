package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.model.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToModel(UserDto userDto) {

        return User
                .builder()
                .id(userDto.getId())
                .birthDay(userDto.getBirthDay())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .pay(userDto.getPay())
                .telephone(userDto.getTelephone())
                .address(userDto.getAddress()).build();
    }

    public Data userToResponse(User user) {
        UserDto userDto = UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .pay(user.getPay())
                .email(user.getEmail())
                .address(user.getAddress())
                .birthDay(user.getBirthDay())
                .build();

        return Data
                .builder()
                .data(userDto)
                .build();
    }
}
