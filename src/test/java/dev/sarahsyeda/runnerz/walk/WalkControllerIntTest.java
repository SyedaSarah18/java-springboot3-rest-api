package dev.sarahsyeda.runnerz.walk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalkControllerIntTest {

    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @BeforeEach
    void setup(){
        restClient = RestClient.create("http://localhost:" + randomServerPort);
    }
    
    @Test
    void shouldFindAllWalks(){
        List<Walk> walks = restClient.get()
                .uri("/api/walks")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertEquals(10, walks.size());
    }
}