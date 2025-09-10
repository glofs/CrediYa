package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.request.LoanModel;
import reactor.core.publisher.Mono;

public interface SaveLoanRepository {
    Mono<LoanModel> saveLoan(LoanModel loanStudy);
}
