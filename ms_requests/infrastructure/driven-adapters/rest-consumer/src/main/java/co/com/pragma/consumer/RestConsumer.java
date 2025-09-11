package co.com.pragma.consumer;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.response.Data;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.loan.exception.Constants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class RestConsumer implements ConsultInformationRepository {
    private WebClient webClient;

    @Override
    @CircuitBreaker(name = "consultInformationUser", fallbackMethod = "")
    public Mono<Data> consultInformationUser(InformationUser infoUser) {
        return Mono.just(infoUser)
                .flatMap(informationUser -> webClient
                        .post()
                        .body(Mono.just(informationUser), InformationUser.class)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError,
                                clientResponse ->
                                        Mono.error(new DataNotFoundException(USER_NOT_FOUND)))
                        .bodyToMono(Data.class)
                        .doOnNext(System.out::println)
                        .onErrorResume(e -> Mono.error(new DataNotFoundException("authentication microservice dont up"))));
    }
}
