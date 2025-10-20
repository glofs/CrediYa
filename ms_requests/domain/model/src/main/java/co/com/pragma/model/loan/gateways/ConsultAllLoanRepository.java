package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.model.loan.request.Pagination;
import reactor.core.publisher.Flux;


public interface ConsultAllLoanRepository {
    Flux<LoanModel> findAllLoan(Pagination pagination);
}
