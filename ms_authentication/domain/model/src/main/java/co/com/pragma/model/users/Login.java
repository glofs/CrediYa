package co.com.pragma.model.users;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class Login {
    private String email;
    private String password;
}
