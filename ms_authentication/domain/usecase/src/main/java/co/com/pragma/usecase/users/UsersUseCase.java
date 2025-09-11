package co.com.pragma.usecase.users;

import co.com.pragma.model.users.User;
import co.com.pragma.model.users.gateways.UsersRepository;
import reactor.core.publisher.Mono;


public class UsersUseCase {
    private final UsersRepository usersRepository;

    public UsersUseCase(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Mono<User> save(User user) {
        return usersRepository.save(user);
    }

    public Mono<Boolean> consultUser(String document) {
        return this.usersRepository.existByDocument(document);
    }
}
