package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.r2dbc.entity.LoanEntity;
import co.com.pragma.r2dbc.entity.LoansType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    LoansTypeRepository loansTypeRepository;

    @Mock
    private TransactionalOperator transactionalOperator;

    @Mock
    ObjectMapper mapper;
    private LoanModel loanModel;
    private LoanEntity loanEntity;

    private LoansType loansType;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loanModel = LoanModel
                .builder()
                .id(1)
                .type("HIPOTECARIO")
                .term("12")
                .document("123456")
                .amount(1208000L)
                .build();

        loanEntity = LoanEntity.builder()
                .id(1)
                .type(loanModel.getType())
                .term(loanModel.getTerm())
                .document(loanModel.getDocument())
                .amount(loanModel.getAmount())
                .build();

        loansType=LoansType
                .builder()
                .name("HIPOTECARIO")
                .amountMax(12L)
                .amountMin(6L)
                .automaticValidation(true)
                .idTypeLoan(1)
                .interestRate(1L)
                .build();

    }

    @Test
    void saveEntity() {
        repositoryAdapter = new MyReactiveRepositoryAdapter(repository, mapper, loansTypeRepository, transactionalOperator);
        when(loansTypeRepository.findByName(loanModel.getType())).thenReturn(Mono.just(loansType));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repositoryAdapter.findByType(loanModel)).thenReturn(Mono.just(loansType));
        when(mapper.map(any(LoanModel.class), eq(LoanEntity.class))).thenReturn(loanEntity);
        when(repository.save(loanEntity)).thenReturn(Mono.just(loanEntity));
        when(mapper.map(any(LoanEntity.class), eq(LoanModel.class))).thenReturn(loanModel);

        repositoryAdapter.saveLoan(loanModel).as(StepVerifier::create)
                .expectNext(loanModel)
                .verifyComplete();
    }
    /*

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findByExample("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save("test")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.save("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }*/
}
