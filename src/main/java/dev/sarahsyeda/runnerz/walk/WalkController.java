package dev.sarahsyeda.runnerz.walk;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/walks")
public class WalkController {

//    @GetMapping("/hello")
//    String home() {
//        return "Hello, Walkerzz!";
//    }

    private final WalkRepository walkRepository;

    public WalkController(WalkRepository walkRepository){
        this.walkRepository = walkRepository;
    }

    @GetMapping("")
    List<Walk> findAll(){
        return walkRepository.findAll();
    }


    @GetMapping("/{id}")
    Walk findById(@PathVariable Integer id){
        Optional<Walk> walk = walkRepository.findById(id);
        if (walk.isEmpty()){
            throw new WalkNotFoundException();
        }
        return walk.get();
    }

    // post

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Walk walk){
        walkRepository.save(walk);
    }

    // put
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Walk walk,@PathVariable Integer id){
        walkRepository.save(walk);
    }

    // delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        walkRepository.delete(walkRepository.findById(id).get());
    }

    @GetMapping("/location/{location}")
    List<Walk> findByLocation(@PathVariable String location){
        return walkRepository.findAllByLocation(location);
    }

}
