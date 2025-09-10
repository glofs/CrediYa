package co.com.pragma.r2dbc.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Table(name = "loans_type")
public class LoansType {
    @Id
    @Column("id_type_loan")
    private Integer idTypeLoan;
    private String name;
    @Column("amount_min")
    private Long amountMin;
    @Column("amount_max")
    private Long amountMax;
    @Column("interest_rate")
    private Long interestRate;
    @Column("automatic_validation")
    private Boolean automaticValidation;
}
