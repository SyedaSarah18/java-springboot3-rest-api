package dev.sarahsyeda.runnerz.walk;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WalkRepository extends ListCrudRepository<Walk, Integer> {

    List<Walk> findAllByLocation(String Location);
}
