package co.com.pragma.log;

import co.com.pragma.model.users.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggerBuilderTest {

    private Logger mockLogger;
    private LoggerBuilder loggerBuilder;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class);
        loggerBuilder = new LoggerBuilder(mockLogger);
    }

    @Test
    void writeInfo_shouldCallLoggerInfoWithExpectedFormat() {

        String data = "TestData";
        String className = "TestClass";
        String message = "START";

        loggerBuilder.writeInfo(data, className, message);

        verify(mockLogger).info("Information {} in {} {} ", data, className, message);
    }


    @Test
    void writeError_shouldCallLoggerErrorWithExpectedFormat() {
        String path = "/api/v1/users/createUser";
        String message = "START";

        loggerBuilder.writeError(message, path);

        verify(mockLogger).error(argThat(logged ->
                logged.contains(path) &&
                        logged.contains(message)
        ));
    }
}