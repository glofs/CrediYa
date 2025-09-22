package co.com.pragma.api;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.LoginDto;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.mapper.LoginMapper;
import co.com.pragma.model.users.Login;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LoginMapperTest {

    private final LoginMapper loginMapper = new LoginMapper();


    @Test
    void login_dto_to_login() {
        LoginDto loginDto = LoginDto
                .builder()
                .email("test@test.com")
                .password("1234")
                .build();

        Login login = loginMapper.loginDtoToLogin(loginDto);

        assertEquals(loginDto.getPassword(), login.getPassword());
        assertEquals(loginDto.getEmail(), login.getEmail());
    }

    @Test
    void String_to_data_login() {
        String token = "1234";
        LoginResponse loginResponse = LoginResponse
                .builder()
                .token(token)
                .build();
        Data<LoginResponse> data = Data
                .<LoginResponse>builder()
                .data(loginResponse)
                .build();

        Data<LoginResponse> loginResponseData=loginMapper.token(token);

        assertEquals(token,loginResponseData.getData().getToken());
    }
}
