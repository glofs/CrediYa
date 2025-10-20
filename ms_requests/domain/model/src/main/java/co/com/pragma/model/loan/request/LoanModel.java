package co.com.pragma.model.loan.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class LoanModel {
    private Integer id;
    private String document;
    private Long amount;
    private String term;
    private String type;
    private String state;
}
