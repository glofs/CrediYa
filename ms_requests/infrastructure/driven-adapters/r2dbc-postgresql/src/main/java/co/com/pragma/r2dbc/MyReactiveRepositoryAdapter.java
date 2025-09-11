package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.SaveLoanRepository;
import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.r2dbc.entity.LoanEntity;
import co.com.pragma.r2dbc.entity.LoansType;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

import static co.com.pragma.model.loan.exception.Constants.*;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<LoanModel, LoanEntity, BigInteger, MyReactiveRepository> implements SaveLoanRepository {
    private final LoansTypeRepository loansTypeRepository;
    private final TransactionalOperator transactionalOperator;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, LoansTypeRepository loansTypeRepository, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, LoanModel.class/* change for domain model */));
        this.loansTypeRepository = loansTypeRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<LoanModel> saveLoan(LoanModel loanModel) {
        loanModel.setIdState(PENDING_OF_REVIEW);
        return findByType(loanModel)
                .as(transactionalOperator::transactional)
                .flatMap(loan -> repository.save(toData(loanModel))
                        .as(transactionalOperator::transactional)
                        .map(this::toEntity))
                .switchIfEmpty(Mono.error(new DataNotFoundException(DATA_IS_BLANK)))
                .onErrorResume(e -> Mono.error(new DataNotFoundException(e.getMessage())));
    }

    public Mono<LoansType> findByType(LoanModel loanModel) {
        return loansTypeRepository.findByName(loanModel.getType())
                .as(transactionalOperator::transactional)
                .switchIfEmpty(Mono.error(new DataNotFoundException(INVALID_TYPE_LOAN)));
    }
}
