package co.com.pragma.usecase.users;

import co.com.pragma.model.users.Login;
import co.com.pragma.model.users.User;
import co.com.pragma.model.users.gateways.LoginRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUserUseCase {
    private final LoginRepository loginRepository;


    public Mono<User> loginUser(Login login) {
        return loginRepository.loginUser(login);
    }
}
