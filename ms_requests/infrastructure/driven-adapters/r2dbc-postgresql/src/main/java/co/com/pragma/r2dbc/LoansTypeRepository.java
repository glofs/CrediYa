package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoansType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoansTypeRepository extends ReactiveCrudRepository<LoansType, Integer> {
    //@Query("SELECT * FROM loans_type WHERE name = $1")
    Mono<LoansType>findByName(String name);
}