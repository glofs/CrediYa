package co.com.pragma.consumer;


import co.com.pragma.model.loan.request.InformationUser;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;


class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;


    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient
                .builder()
                .baseUrl(mockBackEnd.url("/")
                        .toString())
                .build();
        restConsumer = new RestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {

        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function to consultInformationUser")
    void validateTestPost() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"data\" : {\"name\":\"Gustavo\",\"email\":\"a@a.com\"}}"));
        var response = restConsumer.consultInformationUser(new InformationUser(),"ABC","USER");

        StepVerifier.create(response)
                .expectNextMatches(objectResponse -> !objectResponse.getData().getName().isBlank())
                .verifyComplete();
    }

    @Test
    @DisplayName("exception in consultInformationUser")
    void exceptionTestPost() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("{\"data\" : {\"exist\":\"true}}"));
        var response = restConsumer.consultInformationUser(new InformationUser(),"ejb","USER");

        StepVerifier.create(response)
                .expectErrorMatches(e->e.getMessage().equals("Failed to consult information"))
                //2.expectNextMatches(objectResponse -> objectResponse.equals("ok"))
                .verify();
    }
}