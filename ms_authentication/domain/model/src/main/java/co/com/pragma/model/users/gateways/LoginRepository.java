package co.com.pragma.model.users.gateways;

import co.com.pragma.model.users.Login;
import co.com.pragma.model.users.User;
import reactor.core.publisher.Mono;

public interface LoginRepository {
    Mono<User> loginUser(Login login);
}
