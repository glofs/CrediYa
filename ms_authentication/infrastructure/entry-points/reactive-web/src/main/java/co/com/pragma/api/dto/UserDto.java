package co.com.pragma.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private Integer id;
    @NotBlank(message = "First name is required")
    private String name;
    @NotBlank(message = "is required")
    private String document;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDay;
    private String address;
    private String telephone;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0", message = "Base salary must be >= 0")
    @DecimalMax(value = "15000000", message = "Base salary must be <= 15000000")
    private Integer pay;

}
