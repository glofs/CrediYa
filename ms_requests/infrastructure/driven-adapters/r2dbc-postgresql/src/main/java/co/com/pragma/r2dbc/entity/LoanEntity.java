package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Table(name = "loan_entity")
public class LoanEntity {
    @Id
    private Integer id;
    private String document;
    private Long amount;
    private String term;
    private String type;
    private String state;
}
