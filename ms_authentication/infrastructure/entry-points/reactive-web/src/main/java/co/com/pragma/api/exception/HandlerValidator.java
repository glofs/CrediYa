package co.com.pragma.api.exception;

import co.com.pragma.api.dto.UserDto;
import co.com.pragma.log.LoggerBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

import static co.com.pragma.log.Constants.*;

@AllArgsConstructor
@Component
public class HandlerValidator {
    private final Validator validator;
    private final LoggerBuilder loggerBuilder;


    public <T> Mono<T> validate(T objet) {
        loggerBuilder.writeInfo(objet, HandlerValidator.class.getName(), VALIDATION_START);
        return Mono.just(objet).flatMap(createUserDTO -> {
                    Set<ConstraintViolation<T>> violations = validator.validate(objet);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(createUserDTO);
                }).doOnError(
                        e -> loggerBuilder.writeError(e.getMessage(), PATH_CREATE_USERS))
                .doOnNext(userDto -> loggerBuilder.writeInfo(objet, HandlerValidator.class.getName(), VALIDATION_COMPLETE));
    }
}