package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.gateways.SaveLoanRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.model.loan.response.Data;
import co.com.pragma.model.loan.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

public class LoanStudyUseCaseTest {

    @InjectMocks
    private LoanStudyUseCase loanStudyUseCase;

    @Mock
    ConsultInformationRepository consultInformationRepository;

    @Mock
    SaveLoanRepository saveLoanRepository;
    private LoanModel loanModel;
    private InformationUser informationUser;
    private Data data;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(
                this
        );

        loanModel = LoanModel
                .builder()
                .id(1)
                .type("HIPOTECARIO")
                .term("12")
                .document("123456")
                .amount(1208000L)
                .build();

        informationUser = InformationUser
                .builder()
                .document(loanModel.getDocument())
                .build();

        userResponse = UserResponse.
                builder()
                .exist(true)
                .build();

        data = Data.
                builder()
                .data(userResponse)
                .build();

    }

    @Test
    void saveLoanSuccess() {
        when(consultInformationRepository.consultInformationUser(informationUser)).thenReturn(Mono.just(data));
        when(saveLoanRepository.saveLoan(loanModel)).thenReturn(Mono.just(loanModel));

        StepVerifier.create(saveLoanRepository.saveLoan(loanModel))
                .expectNext(loanModel)
                .verifyComplete();

    }

    @Test
    void saveLoanException() {
        when(consultInformationRepository.consultInformationUser(null)).thenReturn(Mono.error(new DataNotFoundException("user not foud")));
        when(saveLoanRepository.saveLoan(loanModel)).thenReturn(Mono.just(loanModel));

        StepVerifier.create(consultInformationRepository.consultInformationUser(null))
                .expectErrorMessage("user not foud")
                .verify();


    }

  /*  @Test
    void saveLoanExceptionEmpty() {
        when(consultInformationRepository.consultInformationUser(informationUser).thenReturn(Mono.empty()));
        StepVerifier.create(loanStudyUseCase.generateLoan(loanModel))
                .expectErrorMatches(e -> e instanceof DataNotFoundException && e.getMessage().equals(CONSULT_USER_IS_EMPTY))
                .verify();

    }*/


}
