package co.com.pragma.model.loan.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanComplete {
    private Pagination metadata;
    private List<LoanEnrichment> loanEnrichment;
}
