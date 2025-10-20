package co.com.pragma.model.users.gateways;


import co.com.pragma.model.users.User;
import reactor.core.publisher.Mono;

public interface UsersRepository {
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String users);
    Mono<Boolean> existByDocument(String document);
    Mono<User> findByDocument(String document);
}
