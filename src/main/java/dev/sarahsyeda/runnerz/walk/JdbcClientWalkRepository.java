package dev.sarahsyeda.runnerz.walk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;


@Repository
public class JdbcClientWalkRepository {
    
    /* IN-MEMORY METHOD
//    private List<Walk> walks = new ArrayList<>();
//    List<Walk> findAll(){
//        return walks;
//    }
//
//    Optional<Walk> findById(Integer id){
//        return walks.stream()
//                .filter(walk -> walk.id() == id)
//                .findFirst();
//    }
//
//    void create(Walk walk) {
//        walks.add(walk);
//    }
//
//    void update(Walk walk, Integer id){
//        Optional<Walk> existingWalk = findById(id);
//        if(existingWalk.isPresent()){
//            walks.set(walks.indexOf(existingWalk.get()),walk);
//        }
//    }
//
//    void delete(Integer id){
//        walks.removeIf(walk -> walk.id().equals(id));
//    }
//
//    @PostConstruct
//    private void init(){
//        walks.add(new Walk(1,
//                "Monday Morning Walk",
//                LocalDateTime.now(),
//                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
//                3,
//                Location.INDOOR));
//
//        walks.add(new Walk(2,
//                "Wednesday Evening Walk",
//                LocalDateTime.now(),
//                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
//                6,
//                Location.INDOOR));
//    }
      */
        

    private static final Logger log = LoggerFactory.getLogger(JdbcClientWalkRepository.class);

    private final JdbcClient jdbcClient;

    public JdbcClientWalkRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Walk> findAll() {
        return jdbcClient.sql("select * from walk")
                .query(Walk.class)
                .list();

    }

    public Optional<Walk> findById(Integer id){
        return jdbcClient.sql("SELECT id,title,started_on,completed_on,distance_km,location FROM Walk WHERE id = :id")
                .param("id",id)
                .query(Walk.class)
                .optional();
    }

    public void create(Walk walk){
        var updated = jdbcClient.sql("INSERT INTO Walk(id,title,started_on,completed_on,distance_km,location) values(?,?,?,?,?,?)")
                .params(List.of(walk.id(),walk.title(),walk.startedOn(),walk.completedOn(),walk.distanceKm(),walk.location().toString()))
                .update();

        Assert.state(updated==1, "Failed to create walk");
    }

    public void update(Walk walk, Integer id){
        var updated = jdbcClient.sql("update walk set title = ?, started_on = ?, completed_on = ?, distance_km = ?, location = ? where id = ?")
                .params(List.of(walk.title(), walk.startedOn(), walk.completedOn(), walk.distanceKm(), walk.location().toString(), id))
                .update();
        Assert.state(updated==1, "Failed to update walk " + walk.title());
    }

    public void delete(Integer id){
        var updated = jdbcClient.sql("delete from walk where id = :id")
                .param("id", id)
                .update();
        Assert.state(updated==1, "Failed to delete walk" + id);
    }

    public int count() {return jdbcClient.sql("select * from walk").query().listOfRows().size();}

    public void saveAll(List<Walk> walks) {walks.stream().forEach(this::create);}

    public List<Walk> findByLocation(String location){
        return jdbcClient.sql("select * from walk where location = :location")
                .param("location", location)
                .query(Walk.class)
                .list();
    }
}
