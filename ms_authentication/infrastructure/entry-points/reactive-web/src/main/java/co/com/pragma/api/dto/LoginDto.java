package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @NotBlank(message = "is required")
    private String email;
    @NotBlank(message = "is required")
    private String password;
}
