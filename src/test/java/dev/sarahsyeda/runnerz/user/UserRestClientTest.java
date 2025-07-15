package dev.sarahsyeda.runnerz.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserRestClient.class)
class UserRestClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    UserRestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFindAllUsers() throws JsonProcessingException {
        // given
        User user1 = new User(1,
                "Leanne",
                "lgraham",
                "lgraham@gmail.com",
                new Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", new Geo("-37.3159", "81.1496")),
                "1-770-736-8031 x56442",
                "hildegard.org",
                new Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        List<User> users = List.of(user1);

        // when
        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(users), MediaType.APPLICATION_JSON));

        // then
        List<User> allUsers = client.findAll();
        assertEquals(users, allUsers);
    }

    @Test
    void shouldFindUserById() throws JsonProcessingException {
        // given
        User user = new User(1,
                "Leanne",
                "lgraham",
                "lgraham@gmail.com",
                new Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", new Geo("-37.3159", "81.1496")),
                "1-770-736-8031 x56442",
                "hildegard.org",
                new Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // when
        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users/1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        // then
        User leanne = client.findById(1);
        assertEquals("Leanne", user.name(), "User name should be Leanne");
        assertEquals("lgraham", user.username(), "User username should be lgraham");
        assertEquals("lgraham@gmail.com", user.email());
        assertAll("Address",
                () -> assertEquals("Kulas Light", user.address().street()),
                () -> assertEquals("Apt. 556", user.address().suite()),
                () -> assertEquals("Gwenborough", user.address().city()),
                () -> assertEquals("92998-3874", user.address().zipcode()),
                () -> assertEquals("-37.3159", user.address().geo().lat()),
                () -> assertEquals("81.1496", user.address().geo().lng())
        );
        assertEquals("1-770-736-8031 x56442", user.phone());
        assertEquals("hildegard.org", user.website());
        assertAll("Company",
                () -> assertEquals("Romaguera-Crona", user.company().name()),
                () -> assertEquals("Multi-layered client-server neural-net", user.company().catchPhrase()),
                () -> assertEquals("harness real-time e-markets", user.company().bs()));
    }


}