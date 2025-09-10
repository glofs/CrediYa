package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.mapper.LoanStudyMapper;
import co.com.pragma.log.Constants;
import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.model.loan.response.LoanResponse;
import co.com.pragma.usecase.loan.LoanStudyUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LoanRouterRest.class, LoanHandler.class})
@WebFluxTest
class LoanRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanStudyUseCase loanStudyUseCase;

    @MockitoBean
    private LoanStudyMapper loanStudyMapper;

    private LoanDto loanDto;
    private LoanModel loanModel;
    private LoanResponse loanResponse;

    @MockitoBean
    private HandlerValidator validator;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loanDto = LoanDto
                .builder()
                .id(1)
                .type("HIPOTECARIO")
                .amount(128900L)
                .term("12")
                .document("123456")
                .build();

        loanModel = LoanModel.builder()
                .term("12")
                .document("123456")
                .type("HIPOTECARIO")
                .amount(128900L)
                .build();

        loanResponse = LoanResponse
                .builder()
                .data(loanModel)
                .code(Constants.OK)
                .build();
    }


    @Test
    void testListenPostUseCase() {
        when(validator.validate(any(LoanDto.class))).thenReturn(Mono.just(loanDto));
        when(loanStudyMapper.LoanDtoToLoanModel(loanDto)).thenReturn(loanModel);
        when(loanStudyUseCase.generateLoan(loanModel)).thenReturn(Mono.just(loanModel));
        when(loanStudyMapper.LoanModelToResponse(loanModel)).thenReturn(loanResponse);
        webTestClient.post()
                .uri("/api/v1/user/loan")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(loanDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(e -> System.out.println("body" + e.toString()))
                .jsonPath("$.data.document").isEqualTo("123456");
    }

    @Test
    void testListenPostException() {
        when(validator.validate(any(LoanDto.class))).thenReturn(Mono.just(loanDto));
        when(loanStudyMapper.LoanDtoToLoanModel(loanDto)).thenReturn(loanModel);
        when(loanStudyUseCase.generateLoan(loanModel)).thenReturn(Mono.error(new DataNotFoundException("User Not Found")));

        webTestClient.post()
                .uri("/api/v1/user/loan")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(loanModel)
                .exchange()
                .expectStatus().is5xxServerError();
                /* .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );*/
    }
}
