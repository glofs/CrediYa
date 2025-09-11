package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.mapper.LoanStudyMapper;
import co.com.pragma.usecase.loan.LoanStudyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @Operation(
            operationId = "save loan",
            summary = "Register new study loan",
            description = "Receive a study loan to save ",
            requestBody = @RequestBody(
                    required = true,
                    description = "loan study  to be registered",
                    content = @Content(schema = @Schema(implementation = LoanDto.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )

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
