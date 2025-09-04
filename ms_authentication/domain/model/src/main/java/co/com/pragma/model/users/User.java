package co.com.pragma.model.users;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthDay;
    private Integer pay;
    private String telephone;
    private String address;

}
