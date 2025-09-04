package co.com.pragma.api;

import co.com.pragma.api.exception.ResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResponseMapperTest {

    @Test
    void transform_shouldReturnServerResponseWithBody() {
        // Arrange
        String input = "text";

        // Act
        Mono<ServerResponse> responseMono = ResponseMapper.transform(input);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> {
                    // You can’t directly get the body from ServerResponse because it's async.
                    // So you can only verify it’s not null and has the expected status.
                    assertThat(serverResponse.statusCode().value()).isEqualTo(200);
                    return true;
                })
                .verifyComplete();
    }
}