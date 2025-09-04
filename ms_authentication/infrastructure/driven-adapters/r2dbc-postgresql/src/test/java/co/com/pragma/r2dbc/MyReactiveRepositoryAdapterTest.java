package co.com.pragma.r2dbc;

import co.com.pragma.model.users.User;
import co.com.pragma.r2dbc.entity.UsersEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

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
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;


    private User user;
    private UsersEntity users2;
    private UsersEntity usersEntity;
    private Flux<User> usersFlux;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .name("Gustavo")
                .lastName("Lozada")
                .birthDay(LocalDate.of(1991, 1, 8))
                .address("calle 8")
                .telephone("3001234567")
                .email("john.doe@test.com")
                .pay(4000000)
                .build();

        users2 = UsersEntity.builder()
                .id(1)
                .name("Gustavo")
                .lastName("Lozada")
                .birthDay(LocalDate.of(1991, 1, 8))
                .address("calle 8")
                .telephone("3001234567")
                .email("john.doe@test.com")
                .pay(4000000)
                .build();
    }

    @Test
    void createUsers() {
        when(repository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(mapper.map(any(User.class), eq(UsersEntity.class))).thenReturn(users2);
        when(repository.save(users2)).thenReturn(Mono.just(users2));
        when(mapper.map(any(UsersEntity.class), eq(User.class))).thenReturn(user);
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MyReactiveRepositoryAdapter adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        adapter.save(user).as(StepVerifier::create)
                .expectNext(user)
                .verifyComplete();
    }


    @Test
    void notCreateUsers() {
        //amtes de hacer guardado lanza excepción, no requiero mock
        when(repository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        //when(mapper.map(any(Users.class), eq(UsersEntity.class))).thenReturn(users2);
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MyReactiveRepositoryAdapter adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        adapter.save(user).as(StepVerifier::create)
                .expectError(IllegalAccessError.class);
    }


    @Test
    void mustFindAllValues() {
        List<UsersEntity> usersList = List.of(users2);
        Flux<UsersEntity> flux = Flux.fromIterable(usersList);
        when(repository.findAll()).thenReturn(flux);
        MyReactiveRepositoryAdapter adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        when(mapper.map(any(UsersEntity.class), eq(User.class))).thenReturn(user);


        adapter.findAll().as(StepVerifier::create)
                .expectNext(user)
                .verifyComplete();
    }


    @Test
    void shouldCheckEmailNotExists() {
        when(repository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MyReactiveRepositoryAdapter adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        adapter.existsByEmail("john.doe@test.com")
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();
    }


    @Test
    void shouldCheckEmailExists() {
        when(repository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MyReactiveRepositoryAdapter adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        adapter.existsByEmail("john.doe@test.com")
                .as(StepVerifier::create)
                .expectNext(false)
                .verifyComplete();
    }
       /*
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
                */


}
