package micro.meetups;

import javax.inject.*;
import java.util.*;
import java.util.stream.*;
import org.neo4j.driver.v1.*;

@Singleton
public class CityRepository {
   private Driver driver;

   private final Map<Long, City> store = new HashMap<>();

   public @Inject CityRepository(Driver driver) { this.driver = driver; }
   public void save(List<City> cities) {
      cities.forEach(c -> store.put(c.id, c));
      try (Session s = driver.session()) {
	      String statement = "UNWIND $cities as row MERGE (c:City {id:row.id}) ON CREATE SET c+=row";
	      Map<String,Object> params = Collections.singletonMap("cities",cities.stream().map(c -> c.asMap()).collect(Collectors.toList()));
          s.writeTransaction(tx -> tx.run(statement, params));
      }
   }
   public Stream<City> findByName(String name) {
      return store.values().stream().filter(c -> c.city.contains(name));
   }
   public Stream<Map<String,Object>> findByCountry(String name) {
      try (Session s = driver.session()) {
	      String statement = "MATCH (c:City) WHERE c.country contains $name RETURN properties(c)";
          return s.readTransaction(tx -> tx.run(statement, Collections.singletonMap("name",name)).list(r -> r.asMap())).stream();
      }
   }
}