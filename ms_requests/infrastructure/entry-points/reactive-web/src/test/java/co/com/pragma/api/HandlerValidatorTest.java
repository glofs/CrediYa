package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.dto.LoanDto;
import co.com.pragma.api.exception.HandlerValidator;
import co.com.pragma.log.LoggerBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerValidatorTest {

    @Mock
    private Validator validator;

    @Mock
    private LoggerBuilder loggerBuilder;

    @InjectMocks
    private HandlerValidator handlerValidator;

    private LoanDto validUser;
    private LoanDto invalidUser;

    @BeforeEach
    void setUp() {
        validUser = new LoanDto();
        invalidUser = new LoanDto();
    }

    @Test
    void validate_ShouldReturnMono_WhenValid() {


        when(validator.validate(validUser)).thenReturn(Collections.emptySet());

        StepVerifier.create(handlerValidator.validate(validUser))
                .expectNext(validUser)
                .verifyComplete();

        //verify(loggerBuilder).writeInfo(validUser, HandlerValidator.class.getName(), "VALIDATION_START");
        //verify(loggerBuilder).writeInfo(validUser, HandlerValidator.class.getName(), "VALIDATION_COMPLETE");
        //verifyNoMoreInteractions(loggerBuilder);
    }

    @Test
    void validate_ShouldReturnError_WhenInvalid() {

        ConstraintViolation<LoanDto> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<LoanDto>> violations = Set.of(violation);
        when(validator.validate(invalidUser)).thenReturn(violations);


        StepVerifier.create(handlerValidator.validate(invalidUser))
                .expectError(ConstraintViolationException.class)
                .verify();
}
}