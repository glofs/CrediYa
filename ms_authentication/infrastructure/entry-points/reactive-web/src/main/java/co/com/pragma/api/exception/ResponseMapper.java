package co.com.pragma.api.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ResponseMapper {

    public static <T> Mono<ServerResponse> transform(T us) {
        return ServerResponse.ok().bodyValue(us);
    }
}
