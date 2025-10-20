package co.com.pragma.model.users.exception;

import lombok.Getter;

@Getter
public class DynamicBusinessException extends RuntimeException {
    private final String message;
    private final int code;

    public DynamicBusinessException(String message, int code) {
        super();
        this.message = message;
        this.code = code;
    }
}
