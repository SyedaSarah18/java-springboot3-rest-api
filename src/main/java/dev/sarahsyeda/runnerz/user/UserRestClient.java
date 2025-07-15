package dev.sarahsyeda.runnerz.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(RestClient.Builder builder){
        JdkClientHttpRequestFactory jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory();
        jdkClientHttpRequestFactory.setReadTimeout(5000);

        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .requestFactory(jdkClientHttpRequestFactory)
                .defaultHeader("User-Agent", "Spring 5 WebClient")
//                .requestInterceptor()
                .build();
    }

    public List<User> findAll(){
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {});
    }

    public User findById(Integer id){
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(User.class);
    }

}
