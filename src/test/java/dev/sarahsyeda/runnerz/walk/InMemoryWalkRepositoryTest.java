package dev.sarahsyeda.runnerz.walk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWalkRepositoryTest {
    InMemoryWalkRepository repository;

    @BeforeEach
    void setUp(){
        repository = new InMemoryWalkRepository();
        repository.create(new Walk(1,
                "Monday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));

        repository.create(new Walk(2,
                "Wednesday Evening Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                6,
                Location.INDOOR));
    }

    @Test
    void shouldFindAllWalks(){
        List<Walk> walks = repository.findAll();
        assertEquals(2, walks.size(), "Should have returned 2 runs");
    }

    @Test
    void shouldFindRunWithValidId() {
        var walk = repository.findById(1).get();
        assertEquals("Monday Morning Walk", walk.title());
        assertEquals(3, walk.distanceKm());
    }

    @Test
    void shouldNotFindRunWithInvalidId() {
        WalkNotFoundException notFoundException = assertThrows(
                WalkNotFoundException.class,
                () -> repository.findById(3).get()
        );

        assertEquals("Walk Not Found", notFoundException.getMessage());
    }

    @Test
    void shouldCreateNewRun() {
        repository.create(new Walk(3,
                "Friday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));
        List<Walk> walks = repository.findAll();
        assertEquals(3, walks.size());
    }

    @Test
    void shouldUpdateRun() {
        repository.update(new Walk(1,
                "Monday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                5,
                Location.OUTDOOR), 1);
        var walk = repository.findById(1).get();
        assertEquals("Monday Morning Walk", walk.title());
        assertEquals(5, walk.distanceKm());
        assertEquals(Location.OUTDOOR, walk.location());
    }

    @Test
    void shouldDeleteRun() {
        repository.delete(1);
        List<Walk> walks = repository.findAll();
        assertEquals(1, walks.size());
    }
}