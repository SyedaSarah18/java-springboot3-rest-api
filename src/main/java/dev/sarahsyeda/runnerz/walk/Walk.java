package dev.sarahsyeda.runnerz.walk;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public record Walk(
        @Id
        Integer id,
        String title,
        LocalDateTime startedOn,
        LocalDateTime completedOn,
        @Positive
        Integer distanceKm,
        Location location
//        @Version : Commented because don't want to use Spring Data part of it during testing.
//        Integer version
) {

    public Walk {
        if(!completedOn.isAfter(startedOn)){
            throw new IllegalArgumentException("Completed On must be after Started On");
        }
    }
}
