package co.com.pragma.model.loan.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InformationUser {
    private String document;
}
