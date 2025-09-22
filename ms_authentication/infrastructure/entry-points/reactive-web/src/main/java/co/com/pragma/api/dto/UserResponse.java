package co.com.pragma.api.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResponse {
    private Integer id;
    private String name;
    private String document;
    private String lastName;
    private LocalDate birthDay;
    private String address;
    private String telephone;
    private String email;
    private Integer pay;
}
