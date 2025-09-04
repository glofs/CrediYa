package co.com.pragma.model.users;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    void userBuilderTest() {

        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        User user = User.builder()
                .id(1)
                .name("John")
                .lastName("Doe")
                .birthDay(birthdate)
                .address("123 Main St")
                .telephone("555-1234")
                .email("john.doe@example.com")
                .pay(50000)
                .build();

        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastName());
        assertEquals(birthdate, user.getBirthDay());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("555-1234", user.getTelephone());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(50000, user.getPay());
    }
}
