package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.mapper.LoanStudyMapper;
import co.com.pragma.usecase.loan.LoanStudyUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class LoanHandler {
    private final HandlerValidator handlerValidator;
    private final LoanStudyUseCase loanStudyUseCase;
    private final LoanStudyMapper loanStudyMapper;

    public LoanHandler(HandlerValidator handlerValidator, LoanStudyUseCase loanStudyUseCase, LoanStudyMapper loanStudyMapper) {
        this.handlerValidator = handlerValidator;
        this.loanStudyUseCase = loanStudyUseCase;
        this.loanStudyMapper = loanStudyMapper;
    }

    public Mono<ServerResponse> generateLoanReq(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(LoanDto.class)
                .flatMap(handlerValidator::validate)
                .map(loanStudyMapper::LoanDtoToLoanModel)
                .flatMap(loanStudyUseCase::generateLoan)
                .map(loanStudyMapper::LoanModelToResponse)
                .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }
}
