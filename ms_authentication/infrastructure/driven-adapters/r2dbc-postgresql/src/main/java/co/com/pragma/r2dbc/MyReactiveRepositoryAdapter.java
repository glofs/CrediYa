package co.com.pragma.r2dbc;

import co.com.pragma.model.users.User;
import co.com.pragma.model.users.gateways.UsersRepository;
import co.com.pragma.r2dbc.entity.UsersEntity;
import co.com.pragma.r2dbc.helper.DataNotFoundException;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<User, UsersEntity, BigInteger, MyReactiveRepository> implements UsersRepository {
    private TransactionalOperator transactionalOperator;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
    }


    @Override
    public Mono<User> save(User user) {
        return existsByEmail(user.getEmail())
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Email already registered")))
                .flatMap(bool -> repository.save(toData(user))
                        .as(transactionalOperator::transactional)
                        .map(this::toEntity));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository
                .existsByEmail(email)
                .as(transactionalOperator::transactional)
                .map(aBoolean -> !aBoolean);
    }
}
