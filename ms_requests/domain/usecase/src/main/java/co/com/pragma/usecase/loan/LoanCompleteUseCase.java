package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.gateways.ConsultAllLoanRepository;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.request.LoanComplete;
import co.com.pragma.model.loan.request.LoanEnrichment;
import co.com.pragma.model.loan.request.Pagination;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import static co.com.pragma.model.loan.exception.Constants.ROLE_ADVISOR;


public class LoanCompleteUseCase {
    private final ConsultAllLoanRepository consultAllLoanRepository;
    private final ConsultInformationRepository consultInformationRepository;

    public LoanCompleteUseCase(ConsultAllLoanRepository consultAllLoanRepository, ConsultInformationRepository consultInformationRepository) {
        this.consultAllLoanRepository = consultAllLoanRepository;
        this.consultInformationRepository = consultInformationRepository;
    }

    public Mono<LoanComplete> consult(Integer size, Integer page, String authorization) {

        Pagination pagination = Pagination
                .builder()
                .size(size)
                .page(page)
                .build();

        return consultAllLoanRepository
                .findAllLoan(pagination)
                .map(loan -> Tuples.of(loan, pagination))
                .flatMap(tuple2 -> {
                    InformationUser informationUser = InformationUser
                            .builder()
                            .document(tuple2.getT1().getDocument())
                            .build();
                    return consultInformationRepository
                            .consultInformationUser(informationUser, authorization,ROLE_ADVISOR)
                            .map(data -> LoanEnrichment
                                    .builder()
                                    .name(data.getData().getName())
                                    .email(data.getData().getEmail())
                                    .loanInformation(tuple2.getT1())
                                    .build());
                })
                .collectList()
                .map(us -> LoanComplete.builder()
                        .metadata(pagination)
                        .loanEnrichment(us)
                        .build());
    }
}
