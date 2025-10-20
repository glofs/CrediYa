package co.com.pragma.model.loan.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Data {
    private BasicInformation data;
}
