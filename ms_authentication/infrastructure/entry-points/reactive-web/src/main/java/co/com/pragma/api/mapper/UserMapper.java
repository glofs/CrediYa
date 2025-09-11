package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.Document;
import co.com.pragma.api.dto.UserBoolean;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.model.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToModel(UserDto userDto) {

        return User
                .builder()
                .id(userDto.getId())
                .document(userDto.getDocument())
                .birthDay(userDto.getBirthDay())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .pay(userDto.getPay())
                .telephone(userDto.getTelephone())
                .address(userDto.getAddress()).build();
    }

    public Data<UserDto> userToResponse(User user) {
        UserDto userDto = UserDto
                .builder()
                .id(user.getId())
                .document(user.getDocument())
                .name(user.getName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .pay(user.getPay())
                .email(user.getEmail())
                .address(user.getAddress())
                .birthDay(user.getBirthDay())
                .build();

        return Data
                .<UserDto>builder()
                .data(userDto)
                .build();
    }

    public Data<UserBoolean> booleanToResponse(Boolean flag) {
        UserBoolean userBoolean = UserBoolean
                .builder()
                .exist(flag)
                .build();

        return Data
                .<UserBoolean>builder()
                .data(userBoolean)
                .build();
    }
}
