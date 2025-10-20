package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.gateways.SaveLoanRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.request.LoanModel;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.loan.exception.Constants.ROLE_USER;
import static co.com.pragma.model.loan.exception.Constants.NOT_FOUND;
import static co.com.pragma.model.loan.exception.Constants.CONSULT_USER_IS_EMPTY;

public class LoanStudyUseCase {

    private final ConsultInformationRepository consultUserRepository;
    private final SaveLoanRepository saveLoanRepository;

    public LoanStudyUseCase(ConsultInformationRepository loanRepository, SaveLoanRepository saveLoanRepository) {
        this.consultUserRepository = loanRepository;
        this.saveLoanRepository = saveLoanRepository;
    }

    public Mono<LoanModel> generateLoan(LoanModel loanModel, String authorization) {

        InformationUser
                informationUser = InformationUser
                .builder()
                .document(loanModel.getDocument())
                .build();
        return consultUserRepository.consultInformationUser(informationUser, authorization, ROLE_USER)
                .switchIfEmpty(Mono.error(new DataNotFoundException(CONSULT_USER_IS_EMPTY, NOT_FOUND)))
                .doOnSuccess(userResponse -> System.out.println("user information " + userResponse))
                .flatMap(loanMod -> saveLoanRepository.saveLoan(loanModel));
    }
}



