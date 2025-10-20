package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.*;
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
                .address(userDto.getAddress())
                .password(userDto.getPassword()).build();
    }

    public Data<UserResponse> userToResponse(User user) {
        UserResponse userResponse = UserResponse
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
                .<UserResponse>builder()
                .data(userResponse)
                .build();
    }

    public Data<BasicInformation> userToBasic(User user) {
        BasicInformation basicInformation = BasicInformation
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return Data
                .<BasicInformation>builder()
                .data(basicInformation)
                .build();
    }
}
