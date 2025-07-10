package dev.sarahsyeda.runnerz.user;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(RestClient.Builder builder){
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();
    }

}
