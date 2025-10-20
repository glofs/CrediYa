package co.com.pragma.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class    LoanDto {
    private Integer id;
    @NotBlank(message = "is required")
    private String document;
    @DecimalMin(value = "0",message = "amount must be >0")
    private Long amount;
    @NotBlank(message = "is required")
    private String term;
    @NotBlank(message = "is required")
    private String type;


}
