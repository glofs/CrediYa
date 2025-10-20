package co.com.pragma.r2dbc;

import co.com.pragma.model.users.User;
import co.com.pragma.r2dbc.entity.UsersEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


public interface MyReactiveRepository extends ReactiveCrudRepository<UsersEntity, BigInteger>, ReactiveQueryByExampleExecutor<UsersEntity> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByDocument(String document);
    Mono<UsersEntity> findByEmail(String email);
    Mono<UsersEntity> findByDocument(String document);

}
