package dev.sarahsyeda.runnerz.walk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConfiguration
class MockWalkRepository{
    @Bean
    public WalkRepository walkRepository(){
        return Mockito.mock(WalkRepository.class);
    }
}

@WebMvcTest(WalkController.class)
@Import(MockWalkRepository.class)
class WalkControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalkRepository repository;

    private final List<Walk> walks = new ArrayList<>();

    @BeforeEach
    void setUp(){
        walks.add(new Walk(1,
                "Monday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));
    }

    @Test
    void shouldFindAllWalks() throws Exception {
        when(repository.findAll()).thenReturn(walks);
        mvc.perform(MockMvcRequestBuilders.get("/api/walks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(walks.size())));
    }
    @Test
    void shouldFindOneWalk() throws Exception {
        Walk walk = walks.get(0);
        when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(walk));
        mvc.perform(MockMvcRequestBuilders.get("/api/walks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(walk.id())))
                .andExpect(jsonPath("$.title", is(walk.title())))
                .andExpect(jsonPath("$.distanceKm", is(walk.distanceKm())))
                .andExpect(jsonPath("$.location", is(walk.location().toString())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/walks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewWalk() throws Exception {
        var walk = new Walk(null,
                "test",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                1,
                Location.INDOOR);
        mvc.perform(MockMvcRequestBuilders.post("/api/walks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walk))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateWalk() throws Exception {
        var walk = new Walk(null,
                "test",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                1,
                Location.INDOOR);
        mvc.perform(MockMvcRequestBuilders.put("/api/walks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walk))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteWalk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/walks/1"))
                .andExpect(status().isNoContent());
    }
}