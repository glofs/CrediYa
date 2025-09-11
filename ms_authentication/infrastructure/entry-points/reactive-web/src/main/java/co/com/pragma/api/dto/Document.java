package co.com.pragma.api.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Document {
    @NotBlank(message = "Document is required")
    private String document;
}
