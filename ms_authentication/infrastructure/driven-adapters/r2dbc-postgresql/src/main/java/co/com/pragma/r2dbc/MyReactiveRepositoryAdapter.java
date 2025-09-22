package co.com.pragma.r2dbc;

import co.com.pragma.model.users.Login;
import co.com.pragma.model.users.Role;
import co.com.pragma.model.users.User;
import co.com.pragma.model.users.gateways.LoginRepository;
import co.com.pragma.model.users.gateways.UsersRepository;
import co.com.pragma.r2dbc.config.BeansConfig;
import co.com.pragma.r2dbc.entity.UsersEntity;
import co.com.pragma.model.users.exception.DataNotFoundException;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<User, UsersEntity, BigInteger, MyReactiveRepository> implements UsersRepository, LoginRepository {
    private final TransactionalOperator transactionalOperator;
    private final PasswordEncoder passwordEncoder;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, PasswordEncoder passwordEncoder) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Mono<User> save(User user) {

        return existsByEmail(user.getEmail())
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Email already registered")))
                .flatMap(isTrue -> {
                    UsersEntity userEntity = toData(user);
                    userEntity.setRole(Role.USER);
                    userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
                    return repository.save(userEntity);
                })
                .as(transactionalOperator::transactional)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository
                .existsByEmail(email)
                .as(transactionalOperator::transactional)
                .map(aBoolean -> !aBoolean);
    }

    public Mono<Boolean> existByDocument(String document) {
        System.out.println("document "+document);
        return repository.existsByDocument(document)
                .as(transactionalOperator::transactional)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Document not found")));
    }

    @Override
    public Mono<User> loginUser(Login login) {

        return repository.findByEmail(login.getEmail())
                .as(transactionalOperator::transactional)
                .map(this::toEntity)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Email of user not found")))
                .filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new DataNotFoundException("password is incorrect")));
    }
}
