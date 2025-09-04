package co.com.pragma.api;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.model.users.User;
import co.com.pragma.usecase.users.UsersUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private UsersUseCase usersUseCase;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private HandlerValidator validators;

    private User user;
    private UserDto userDto;
    private Data data;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .name("Gustavo")
                .lastName("Lozada")
                .birthDay(LocalDate.of(1991, 1, 8))
                .address("calle 8")
                .telephone("3001234567")
                .email("john.doe@test.com")
                .pay(4000000)
                .build();


        userDto = UserDto.builder()
                .id(1)
                .name("Gustavo")
                .lastName("Lozada")
                .birthDay(LocalDate.of(1991, 1, 8))
                .address("calle 8")
                .telephone("3001234567")
                .email("john.doe@test.com")
                .pay(4000000)
                .build();

        data = Data
                .builder()
                .data(userDto)
                .build();
    }


    @Test
    void createUser() {

        // Given
        UserDto userRequest1 = new UserDto();
        userRequest1.setName("Gustavo");
        userRequest1.setLastName("Lozada");
        userRequest1.setBirthDay(LocalDate.of(1990, 1, 8));
        userRequest1.setAddress("calle 8");
        userRequest1.setTelephone("3001234567");
        userRequest1.setEmail("john.doe@test.com");
        userRequest1.setPay(4000000);

        when(validators.validate(any(UserDto.class))).thenReturn(Mono.just(userRequest1));
        when(userMapper.dtoToModel(userRequest1)).thenReturn(user);
        when(usersUseCase.save(user)).thenReturn(Mono.just(user));
        when(userMapper.userToResponse(user)).thenReturn(data);

        webTestClient.post()
                .uri("/api/v1/users/createUser")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest1)
                .exchange()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Gustavo");
    }

    @Test
    void createUserException() {

        UserDto userRequest1 = new UserDto();
        userRequest1.setName("Gustavo");
        userRequest1.setLastName("Lozada");
        userRequest1.setBirthDay(LocalDate.of(1990, 1, 8));
        userRequest1.setAddress("calle 8");
        userRequest1.setTelephone("3001234567");
        userRequest1.setEmail("john.doe@test.com");
        userRequest1.setPay(4000000);

        when(validators.validate(any(UserDto.class))).thenReturn(Mono.just(userRequest1));
        when(userMapper.dtoToModel(userRequest1)).thenReturn(user);
        when(usersUseCase.save(user)).thenReturn(Mono.error(new RuntimeException("Email user already registered")));

        webTestClient.post()
                .uri("/api/v1/users/createUse")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

}
