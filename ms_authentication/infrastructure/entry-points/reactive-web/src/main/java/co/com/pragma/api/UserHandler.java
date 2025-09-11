package co.com.pragma.api;

import co.com.pragma.api.dto.Document;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.exception.ResponseMapper;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.usecase.users.UsersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserMapper userMapper;
    private final HandlerValidator handlerValidator;
    private final UsersUseCase usersUseCase;

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
                .flatMap(usersUseCase::save)
                .map(userMapper::userToResponse)
                .flatMap(ResponseMapper::transform);
    }

    public Mono<ServerResponse> consultUserByDocument(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(Document.class)
                .flatMap(handlerValidator::validate)
                .map(Document::getDocument)
                .flatMap(usersUseCase::consultUser)
                .map(userMapper::booleanToResponse)
                .flatMap(ResponseMapper::transform);
    }
}
