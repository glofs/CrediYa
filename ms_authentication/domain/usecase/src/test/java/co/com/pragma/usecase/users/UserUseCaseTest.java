package co.com.pragma.usecase.users;

import co.com.pragma.model.users.User;
import co.com.pragma.model.users.gateways.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.when;


public class UserUseCaseTest {

    @InjectMocks
    private UsersUseCase usersUseCase;

    @Mock
    private UsersRepository usersRepository;

    private User userReq;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(
                this
        );

        userReq = new User();
        userReq.setName("Gustavo");
        userReq.setLastName("Lozada");
        userReq.setBirthDay(LocalDate.of(1990, 1, 8));
        userReq.setAddress("calle 8");
        userReq.setTelephone("3001234567");
        userReq.setEmail("john.doe@test.com");
        userReq.setPay(4000000);

    }

    @Test
    public void createUser() {

        when(usersRepository.save(userReq)).thenReturn(Mono.just(userReq));

        StepVerifier.create(usersUseCase.save(userReq))
                .expectNext(userReq)
                .verifyComplete();
    }

    @Test
    public void errorCreateUser() {

        when(usersUseCase.save(null)).thenReturn(Mono.error(new IllegalArgumentException("Email already registered")));

        StepVerifier.create(usersUseCase.save(null))
                .expectErrorMessage("Email already registered")
                .verify();
    }

}

