package co.com.pragma.api.exception;

import co.com.pragma.log.Constants;
import co.com.pragma.model.users.exception.BusinessException;
import co.com.pragma.model.users.exception.DataNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class GlobalExceptionHandle extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandle(ErrorAttributes errorAttributes, WebProperties resources, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::routingFunction);
    }

    protected Mono<ServerResponse> routingFunction(ServerRequest serverRequest) {
        return Mono.just(serverRequest.exchange().getResponse())
                .flatMap(resp -> {
                    if (getError(serverRequest) instanceof ConstraintViolationException e) {
                        return ServerResponse.badRequest().
                                body(BodyInserters.fromValue(BusinessException.builder()
                                        .message(Arrays.asList(e.getMessage().split(",")))
                                        .path(serverRequest.path())
                                        .localDateTime(LocalDateTime.now())
                                        .code(Constants.BAD_REQUEST)
                                        .build()));
                    }
                    if (getError(serverRequest) instanceof DataNotFoundException i) {
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).
                                body(BodyInserters.fromValue(BusinessException.builder()
                                        .message(Arrays.asList(i.getMessage().split(",")))
                                        .path(serverRequest.path())
                                        .localDateTime(LocalDateTime.now())
                                        .code(Constants.INTERNAL_SERVER_ERROR)
                                        .build()));
                    }
                    return Mono.empty();
                });
    }
}
