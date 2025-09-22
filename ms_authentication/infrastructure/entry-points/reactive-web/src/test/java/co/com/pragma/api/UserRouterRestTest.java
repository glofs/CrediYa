package co.com.pragma.api;

import co.com.pragma.api.config.SecurityTestConfig;
import co.com.pragma.api.dto.*;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.jwt.GetAuthority;
import co.com.pragma.api.jwt.JwtService;
import co.com.pragma.api.mapper.LoginMapper;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.model.users.Login;
import co.com.pragma.model.users.User;
import co.com.pragma.usecase.users.LoginUserUseCase;
import co.com.pragma.usecase.users.UsersUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Import(SecurityTestConfig.class)
@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private UsersUseCase usersUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private LoginMapper loginMapper;

    @MockitoBean
    private HandlerValidator validators;
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private GetAuthority getAuthority;

    private User user;
    private UserResponse userResponse;
    private Data<UserResponse> data;
    private Data<UserBoolean> data1;
    private Data<LoginResponse> data2;
    private UserBoolean userBoolean;
    private Document document;
    private LoginDto login;
    private Login login1;
    private LoginResponse loginResponse;


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
                .role("USER")
                .password("1234")
                .build();


        userResponse = UserResponse.builder()
                .id(1)
                .name("Gustavo")
                .lastName("Lozada")
                .birthDay(LocalDate.of(1991, 1, 8))
                .address("calle 8")
                .telephone("3001234567")
                .email("john.doe@test.com")
                .pay(4000000)
                .build();

        login = LoginDto
                .builder()
                .email("john.doe@test.com")
                .password("1234")
                .build();
        login1 = Login
                .builder()
                .email("john.doe@test.com")
                .password("1234")
                .build();

        userBoolean = UserBoolean
                .builder()
                .exist(true)
                .build();

        data = Data
                .<UserResponse>builder()
                .data(userResponse)
                .build();

        data1 = Data.
                <UserBoolean>builder()
                .data(userBoolean)
                .build();

        loginResponse = LoginResponse
                .builder()
                .token("1234")
                .build();

        data2 = Data
                .<LoginResponse>builder()
                .data(loginResponse)
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
        when(getAuthority.roles("ADMIN")).thenReturn(Mono.just("ADMIN"));
        when(usersUseCase.save(user)).thenReturn(Mono.just(user));
        when(userMapper.userToResponse(user)).thenReturn(data);

        webTestClient.post()
                .uri("/api/v1/users/createUser")
                .header(HttpHeaders.AUTHORIZATION, "Bearer 1234")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest1)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.data.name").isEqualTo("Gustavo");
    }

    @Test
    public void consultUser() {


        when(validators.validate(any(Document.class))).thenReturn(Mono.just(document));
        when(getAuthority.roles("USER")).thenReturn(Mono.just("USER"));
        when(usersUseCase.consultUser(document.getDocument())).thenReturn(Mono.just(true));
        when(userMapper.booleanToResponse(true)).thenReturn(data1);


        webTestClient.post()
                .uri("/api/v1/user/consult")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 1234")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(document)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.data.exist").isEqualTo(true);
    }

    @Test
    void login_user() {
        when(validators.validate(any(LoginDto.class))).thenReturn(Mono.just(login));//serialize change instance in bodyvalue de webtest
        when(loginMapper.loginDtoToLogin(login)).thenReturn(login1);
        when(loginUserUseCase.loginUser(login1)).thenReturn(Mono.just(user));
        when(jwtService.generateToken(user)).thenReturn(Mono.just("1234"));
        when(loginMapper.token("1234")).thenReturn(data2);

        webTestClient.post()
                .uri("/api/v1/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(login)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.data.token").isEqualTo("1234");

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
                .header(HttpHeaders.AUTHORIZATION, "Bearer 1234")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
        //match el error concreto
    }

}
