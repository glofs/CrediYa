package co.com.pragma.model.loan.response;

import co.com.pragma.model.loan.request.LoanModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class LoanResponse {
    private String code;
    private LoanModel data;
}
