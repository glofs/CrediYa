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
@Table(name = "states")
public class StateEntity {
    @Id
    @Value(value = "id_state")
    private Integer id;
    private String name;
    private String Description;

}
