package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.gateways.SaveLoanRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.model.loan.response.Data;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

import static co.com.pragma.model.loan.exception.Constants.CONSULT_USER_IS_EMPTY;


public class LoanStudyUseCase {

    private final ConsultInformationRepository consultUserRepository;
    private final SaveLoanRepository saveLoanRepository;

    public LoanStudyUseCase(ConsultInformationRepository loanRepository, SaveLoanRepository saveLoanRepository) {
        this.consultUserRepository = loanRepository;
        this.saveLoanRepository = saveLoanRepository;
    }

    public Mono<?> generateLoan(LoanModel loanModel, String authorization) {

        InformationUser
                informationUser = InformationUser
                .builder()
                .document(loanModel.getDocument())
                .build();

        return consultUserRepository.consultInformationUser(informationUser, authorization)
                .switchIfEmpty(Mono.error(new DataNotFoundException(CONSULT_USER_IS_EMPTY)))
                .doOnSuccess(userResponse -> System.out.println("user information " + userResponse))
                .flatMap(loanMod -> saveLoanRepository.saveLoan(loanModel));
    }
}



