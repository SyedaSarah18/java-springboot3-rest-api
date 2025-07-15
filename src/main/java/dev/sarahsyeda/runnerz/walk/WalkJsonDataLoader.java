package dev.sarahsyeda.runnerz.walk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class WalkJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(WalkJsonDataLoader.class);
    private final WalkRepository walkRepository;
    public final ObjectMapper objectMapper;

    public WalkJsonDataLoader(WalkRepository walkRepository, ObjectMapper objectMapper) {
        this.walkRepository = walkRepository;
        this.objectMapper = objectMapper;
    }



    @Override
    public void run(String... args) throws Exception {
        System.out.println("walkjsondataloader started");
        if(walkRepository.count() == 0){
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/walks.json")){
                //reading json, map that json, deserialize into a list of walks (objects)
                Walks allWalks = objectMapper.readValue(inputStream, Walks.class);
                log.info("Reading {} walks from JSON data and saving to database.", allWalks.walks().size());
                System.out.println("about to save");
                walkRepository.saveAll(allWalks.walks());
            } catch (IOException e){
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading Walks from JSON data because collection contains data already.");
        }
    }
}
