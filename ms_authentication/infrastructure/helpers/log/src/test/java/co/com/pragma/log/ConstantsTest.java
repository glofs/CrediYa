package co.com.pragma.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstantsTest {

    @Test
    void testValidationStartConstant() {
        assertEquals("Start validation of data", Constants.VALIDATION_START);
    }

    @Test
    void testValidationCompleteConstant() {
        assertEquals("Complete validation of data", Constants.VALIDATION_COMPLETE);
    }

    @Test
    void testPathCreateUsersConstant() {

        assertEquals("/api/v1/users/createUser", Constants.PATH_CREATE_USERS);
    }
}