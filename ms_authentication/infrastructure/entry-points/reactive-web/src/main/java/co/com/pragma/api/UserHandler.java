package co.com.pragma.api;

import co.com.pragma.api.jwt.GetAuthority;
import co.com.pragma.api.jwt.JwtService;
import co.com.pragma.api.dto.Document;
import co.com.pragma.api.dto.LoginDto;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.exception.ResponseMapper;
import co.com.pragma.api.mapper.LoginMapper;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.model.users.exception.DataNotFoundException;
import co.com.pragma.usecase.users.LoginUserUseCase;
import co.com.pragma.usecase.users.UsersUseCase;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.util.Tuple;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserMapper userMapper;
    private final LoginMapper loginMapper;
    private final HandlerValidator handlerValidator;
    private final UsersUseCase usersUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final JwtService jwtService;
    private final GetAuthority authority;

    @Operation(
            operationId = "createUsers",
            summary = "Register new user",
            description = "Receive a request information to save the new users",
            requestBody = @RequestBody(
                    required = true,
                    description = "User data to be registered",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )

    public Mono<ServerResponse> createUsers(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UserDto.class)
                .flatMap(handlerValidator::validate)
                .map(userMapper::dtoToModel)
                .flatMap(user -> authority.roles("ADMIN")
                        .flatMap(boo -> usersUseCase.save(user)))
                .map(userMapper::userToResponse)
                .flatMap(ResponseMapper::transform);
    }

    public Mono<ServerResponse> consultUserByDocument(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(Document.class)
                .flatMap(handlerValidator::validate)
                .map(Document::getDocument)
                .flatMap(document ->
                        authority.roles("USER")
                                .map(role -> Tuples.of(document, role)))
                .flatMap(tuples -> usersUseCase.consultUser(tuples.getT1()))
                .map(userMapper::booleanToResponse)
                .flatMap(ResponseMapper::transform);
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDto.class)
                .flatMap(handlerValidator::validate)
                .map(loginMapper::loginDtoToLogin)
                .flatMap(loginUserUseCase::loginUser)
                .flatMap(jwtService::generateToken)
                .map(loginMapper::token)
                .flatMap(ok -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ok));

    }


}
