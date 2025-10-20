package co.com.pragma.model.loan.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class BasicInformation {
    private String name;
    private String email;
}
