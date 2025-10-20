package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultAllLoanRepository;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.gateways.SaveLoanRepository;
import co.com.pragma.model.loan.request.*;
import co.com.pragma.model.loan.response.Data;
import co.com.pragma.model.loan.response.BasicInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoanStudyUseCaseTest {

    @InjectMocks
    private LoanStudyUseCase loanStudyUseCase;

    @InjectMocks
    private LoanCompleteUseCase loanCompleteUseCase;

    @Mock
    ConsultInformationRepository consultInformationRepository;

    @Mock
    SaveLoanRepository saveLoanRepository;

    @Mock
    ConsultAllLoanRepository consultAllLoanRepository;

    private LoanModel loanModel;
    private InformationUser informationUser;
    private Data data;
    private BasicInformation basicInformation;
    private Pagination pagination;
    private LoanEnrichment loanEnrichment;
    private LoanComplete loanComplete;

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
                .document("123456")
                .build();

        basicInformation = BasicInformation.
                builder()
                .name("Gustavo")
                .email("jane.doe@test.com")
                .build();

        data = Data.
                builder()
                .data(basicInformation)
                .build();

        pagination = Pagination
                .builder()
                .size(12)
                .page(1)
                .build();

        loanEnrichment = LoanEnrichment
                .builder()
                .name("Gustavo")
                .email("jane.doe@test.com")
                .loanInformation(loanModel)
                .build();

        loanComplete = LoanComplete
                .builder()
                .metadata(pagination)
                .loanEnrichment(List.of(loanEnrichment))
                .build();

    }

    @Test
    void saveLoanSuccess() {
        when(consultInformationRepository.consultInformationUser(informationUser, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ", "USER")).thenReturn(Mono.just(data));
        when(saveLoanRepository.saveLoan(loanModel)).thenReturn(Mono.just(loanModel));

        StepVerifier.create(saveLoanRepository.saveLoan(loanModel))
                .expectNext(loanModel)
                .verifyComplete();

    }

    @Test
    void saveLoanException() {
        when(consultInformationRepository.consultInformationUser(informationUser, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ", "USER")).thenReturn(Mono.error(new DataNotFoundException("user not foud", 404)));
        when(saveLoanRepository.saveLoan(loanModel)).thenReturn(Mono.just(loanModel));

        StepVerifier.create(consultInformationRepository.consultInformationUser(informationUser, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ","USER"))
                .expectErrorMessage("user not foud")
                .verify();


    }

    @Test
    void saveLoanExceptionEmpty() {

        when(consultInformationRepository.consultInformationUser(informationUser, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ", "USER")).thenReturn(Mono.empty());
        when(saveLoanRepository.saveLoan(loanModel)).thenReturn(Mono.just(loanModel));
        StepVerifier.create(loanStudyUseCase.generateLoan(loanModel, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ"))
                .expectErrorMatches(e -> e instanceof DataNotFoundException && e.getMessage().equals("consult user is empty"))
                .verify();
    }

    @Test
    void loanCompleteSuccess() {
        when(consultAllLoanRepository.findAllLoan(pagination)).thenReturn(Flux.just(loanModel));
        when(consultInformationRepository.consultInformationUser(any(InformationUser.class), any(String.class), any(String.class))).thenReturn(Mono.just(data));
        StepVerifier.create(loanCompleteUseCase.consult(pagination.getSize(), pagination.getPage(), "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ"))
                /*.assertNext(actual -> {
                    System.out.println("Esperado: " + loanComplete);
                    System.out.println("Actual:   " + actual);
                    assertEquals(loanComplete, actual);
                })*/
                .expectNext(loanComplete)
                .verifyComplete();
    }

}
