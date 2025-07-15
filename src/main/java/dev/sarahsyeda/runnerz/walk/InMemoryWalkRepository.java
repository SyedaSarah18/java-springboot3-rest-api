package dev.sarahsyeda.runnerz.walk;

import dev.sarahsyeda.runnerz.walk.Location;
import dev.sarahsyeda.runnerz.walk.Walk;
import dev.sarahsyeda.runnerz.walk.WalkNotFoundException;
import dev.sarahsyeda.runnerz.walk.WalkRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryWalkRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryWalkRepository.class);
    private final List<Walk> walks = new ArrayList<>();

    public List<Walk> findAll() {
        return walks;
    }

    public Optional<Walk> findById(Integer id) {
        return Optional.ofNullable(walks.stream()
                .filter(walk -> walk.id() == id)
                .findFirst()
                .orElseThrow(WalkNotFoundException::new));
    }

    public void create(Walk walk) {
        Walk newWalk = new Walk(walk.id(),
                walk.title(),
                walk.startedOn(),
                walk.completedOn(),
                walk.distanceKm(),
                walk.location());

        walks.add(newWalk);
    }

    public void update(Walk newWalk, Integer id) {
        Optional<Walk> existingWalk = findById(id);
        if(existingWalk.isPresent()) {
            var r = existingWalk.get();
            log.info("Updating Existing Walk: " + existingWalk.get());
            walks.set(walks.indexOf(r),newWalk);
        }
    }

    public void delete(Integer id) {
        log.info("Deleting Walk: " + id);
        walks.removeIf(walk -> walk.id().equals(id));
    }

    public int count() {
        return walks.size();
    }

    public void saveAll(List<Walk> walks) {
        walks.stream().forEach(walk -> create(walk));
    }

    public List<Walk> findByLocation(String location) {
        return walks.stream()
                .filter(walk -> Objects.equals(walk.location(), location))
                .toList();
    }


    @PostConstruct
    private void init() {
        walks.add(new Walk(1,
                "Monday Morning Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));

        walks.add(new Walk(2,
                "Wednesday Evening Walk",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                6,
                Location.INDOOR));
    }


}
