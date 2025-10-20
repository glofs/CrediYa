package co.com.pragma.model.loan.request;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class LoanEnrichment {
    private String name;
    private String email;
    private LoanModel loanInformation;
}
