package dev.sarahsyeda.runnerz.walk;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WalkNotFoundException extends RuntimeException{
    public WalkNotFoundException(){
        super("Walk Not Found");
    }
}
