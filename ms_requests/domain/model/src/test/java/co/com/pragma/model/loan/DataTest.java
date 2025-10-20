package co.com.pragma.model.loan;

import co.com.pragma.model.loan.response.Data;
import co.com.pragma.model.loan.response.BasicInformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    void shouldSetAndGetUserResponse() {
        BasicInformation basicInformation = BasicInformation.builder()
                .name("Gustavo")
                .email("jane.doe@gmail.com")
                .build();

        Data dataWrapper = Data.builder().data(basicInformation).build();

        assertNotNull(dataWrapper.getData());
        assertEquals("jane.doe@gmail.com", dataWrapper.getData().getEmail());
    }
}
