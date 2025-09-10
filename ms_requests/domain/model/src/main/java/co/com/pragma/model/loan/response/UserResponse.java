package co.com.pragma.model.loan.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class UserResponse {
    private boolean exist;
}
