package dev.sarahsyeda.runnerz.walk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(JdbcClientWalkRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcClientWalkRepositoryTest {

    @Autowired
    JdbcClientWalkRepository repository;

    @BeforeEach
    void setUp() {
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
    void shouldFindAllWalks() {
        List<Walk> runs = repository.findAll();
        assertEquals(2, runs.size());
    }

    @Test
    void shouldFindWalkWithValidId() {
        var run = repository.findById(1).get();
        assertEquals("Monday Morning Walk", run.title());
        assertEquals(3, run.distanceKm());
    }

    @Test
    void shouldNotFindWalkWithInvalidId() {
        var run = repository.findById(3);
        assertTrue(run.isEmpty());
    }

    @Test
    void shouldCreateNewWalk() {
        repository.create(new Walk(3,
                "Friday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));
        List<Walk> runs = repository.findAll();
        assertEquals(3, runs.size());
    }

    @Test
    void shouldUpdateWalk() {
        repository.update(new Walk(1,
                "Monday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                5,
                Location.OUTDOOR), 1);
        var run = repository.findById(1).get();
        assertEquals("Monday Morning Walk", run.title());
        assertEquals(5, run.distanceKm());
        assertEquals(Location.OUTDOOR, run.location());
    }

    @Test
    void shouldDeleteWalk() {
        repository.delete(1);
        List<Walk> walks = repository.findAll();
        assertEquals(1, walks.size());
    }

}