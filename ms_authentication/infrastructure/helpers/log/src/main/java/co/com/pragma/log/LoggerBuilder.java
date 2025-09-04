package co.com.pragma.log;


import co.com.pragma.model.users.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoggerBuilder {
    private final Logger logger;

    public LoggerBuilder() {
        this.logger = LoggerFactory.getLogger(LoggerBuilder.class);
    }

    public LoggerBuilder(Logger logger) {
        this.logger = logger;
    }

    public <T> void writeError(String message, String path) {
        BusinessException exception = BusinessException.builder()
                .path(path)
                .localDateTime(LocalDateTime.now())
                .message(List.of(message))
                .build();

        logger.error(String.valueOf(exception));
    }

    public <T> void writeInfo(T data, String className, String message) {
        logger.info("Information {} in {} {} ", data, className, message);
    }
}
