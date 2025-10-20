package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoanEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.math.BigInteger;

public interface MyReactiveRepository extends ReactiveCrudRepository<LoanEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanEntity> {
    Flux<LoanEntity> findAllBy(Pageable pageable);
}
