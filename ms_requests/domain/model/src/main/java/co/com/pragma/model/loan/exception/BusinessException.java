package co.com.pragma.model.loan.exception;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessException  {
    private String code;
    private List<String> message;
    private String path;
    private LocalDateTime localDateTime;

}
