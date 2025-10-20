package co.com.pragma.model.loan.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Metadata {
    private Pagination metadata;
}
