package co.com.pragma.model.loan;

import co.com.pragma.model.loan.response.Data;
import co.com.pragma.model.loan.response.UserResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    void shouldSetAndGetUserResponse() {
        UserResponse userResponse = UserResponse.builder()
                .exist(true)
                .build();

        Data dataWrapper = Data.builder().data(userResponse).build();

        assertNotNull(dataWrapper.getData());
        assertTrue(dataWrapper.getData().isExist());
    }
}
