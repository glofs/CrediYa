package co.com.pragma.r2dbc.entity;

import co.com.pragma.model.users.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users_entity")
public class UsersEntity{
    @Id
    private Integer id;
    @NotBlank(message = "name field is required")
    private String name;
    @NotBlank(message = "document field is required")
    private String document;
    @NotBlank(message = "LastName field is required")
    private String lastName;
    @Email(message = "format email invalid")
    private String email;
    private Integer pay;
    private LocalDate birthDay;
    private String telephone;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
}
