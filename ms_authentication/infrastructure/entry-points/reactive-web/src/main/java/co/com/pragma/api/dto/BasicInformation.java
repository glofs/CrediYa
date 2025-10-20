package co.com.pragma.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class BasicInformation {
    private String name;
    private String email;
}
