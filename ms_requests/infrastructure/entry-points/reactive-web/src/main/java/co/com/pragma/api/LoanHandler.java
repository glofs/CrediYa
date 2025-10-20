package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.api.mapper.LoanStudyMapper;
import co.com.pragma.usecase.loan.LoanCompleteUseCase;
import co.com.pragma.usecase.loan.LoanStudyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class LoanHandler {
    private final HandlerValidator handlerValidator;
    private final LoanStudyUseCase loanStudyUseCase;
    private final LoanStudyMapper loanStudyMapper;
    private final LoanCompleteUseCase loanCompleteUseCase;

    public LoanHandler(HandlerValidator handlerValidator, LoanStudyUseCase loanStudyUseCase, LoanStudyMapper loanStudyMapper, LoanCompleteUseCase loanCompleteUseCase) {
        this.handlerValidator = handlerValidator;
        this.loanStudyUseCase = loanStudyUseCase;
        this.loanStudyMapper = loanStudyMapper;
        this.loanCompleteUseCase = loanCompleteUseCase;
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
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )

    public Mono<ServerResponse> generateLoanReq(ServerRequest serverRequest) {
        String authorization = serverRequest.exchange().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return serverRequest
                .bodyToMono(LoanDto.class)
                .flatMap(handlerValidator::validate)
                .map(loanStudyMapper::LoanDtoToLoanModel)
                .flatMap(study -> loanStudyUseCase.generateLoan(study, authorization))
                .map(loanStudyMapper::LoanModelToResponse)
                .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }

    public Mono<ServerResponse> getRequest(ServerRequest serverRequest) {

        String authorization = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(1);

        return loanCompleteUseCase.consult(size, page, authorization)
                .flatMap(listLoan -> ServerResponse
                        .ok()
                        .bodyValue(listLoan));
    }
}
