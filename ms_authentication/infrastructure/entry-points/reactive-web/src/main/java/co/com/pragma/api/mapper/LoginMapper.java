package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.LoginDto;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.model.users.Login;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public Login loginDtoToLogin(LoginDto loginDto) {
        return Login
                .builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword()).build();
    }

    public Data<LoginResponse> token(String token) {

        LoginResponse loginResponse = LoginResponse
                .builder()
                .token(token)
                .build();
        return Data
                .<LoginResponse>builder()
                .data(loginResponse)
                .build();

    }
}
