package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.log.Constants;
import co.com.pragma.model.loan.request.LoanModel;
import co.com.pragma.model.loan.response.LoanResponse;

public class LoanStudyMapper {

    public  LoanModel LoanDtoToLoanModel(LoanDto loanDto) {

        return LoanModel
                .builder()
                .id(loanDto.getId())
                .document(loanDto.getDocument())
                .amount(loanDto.getAmount())
                .term(loanDto.getTerm())
                .type(loanDto.getType())
                .build();
    }

    public LoanResponse LoanModelToResponse(LoanModel loanModel) {

        return LoanResponse
                .builder()
                .code(Constants.OK)
                .data(loanModel)
                .build();
    }
}
