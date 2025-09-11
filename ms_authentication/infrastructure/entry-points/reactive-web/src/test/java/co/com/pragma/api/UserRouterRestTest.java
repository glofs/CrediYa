package co.com.pragma.api;

import co.com.pragma.api.dto.Data;
import co.com.pragma.api.dto.Document;
import co.com.pragma.api.dto.UserBoolean;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private Data<UserDto> data;
    private Data<UserBoolean> data1;
    private UserBoolean userBoolean;
    private Document document;

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

        userBoolean = UserBoolean
                .builder()
                .exist(true)
                .build();

        data = Data
                .<UserDto>builder()
                .data(userDto)
                .build();

        data1 = Data.
                <UserBoolean>builder()
                .data(userBoolean)
                .build();

        document = Document
                .builder()
                .document("1066515628")
                .build();

    }


    @Test
    void createUser() {

        UserDto userRequest1 = new UserDto();
        userRequest1.setName("Gustavo");
        userRequest1.setLastName("Lozada");
        userRequest1.setBirthDay(LocalDate.of(1990, 1, 8));
        userRequest1.setAddress("calle 8");
        userRequest1.setTelephone("3001234567");
        userRequest1.setEmail("john.doe@test.com");
        userRequest1.setPay(4000000);

        when(validators.validate(any(UserDto.class))).thenReturn(Mono.just(userRequest1));
        //no usar any
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
    public void consultUser() {


        when(validators.validate(any(Document.class))).thenReturn(Mono.just(document));
        when(usersUseCase.consultUser(document.getDocument())).thenReturn(Mono.just(true));
        when(userMapper.booleanToResponse(true)).thenReturn(data1);


        webTestClient.post()
                .uri("/api/v1/user/consult")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(document)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.data.exist").isEqualTo(true);
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
        //borra any
        when(validators.validate(any(UserDto.class))).thenReturn(Mono.just(userRequest1));
        when(userMapper.dtoToModel(userRequest1)).thenReturn(user);
        when(usersUseCase.save(user)).thenReturn(Mono.error(new RuntimeException("Email user already registered")));
        //cambiar excception
        webTestClient.post()
                .uri("/api/v1/users/createUse")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
        //match el error concreto
    }

}
