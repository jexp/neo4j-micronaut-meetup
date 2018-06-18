package micro.meet.city;

import javax.inject.*;
import java.util.*;
import static java.util.Collections.*;
import java.util.stream.*;
import org.neo4j.driver.v1.*;
import io.micronaut.context.annotation.*;

@Primary
@Singleton
public class CityNeo4jRepository implements CityRepository {
   @Inject Driver driver;

   public void save(City city) {
      try (Session s = driver.session()) {
	      String statement = "MERGE (c:City {id:$city.id}) ON CREATE SET c+=$city";
          s.writeTransaction(tx -> tx.run(statement, singletonMap("city", city.asMap())));
      }
   }

   public Stream<City> findByName(String name) {
      try (Session s = driver.session()) {
	      String statement = "MATCH (c:City) WHERE c.city contains $name RETURN c";
          return s.readTransaction(tx -> tx.run(statement, singletonMap("name",name)))
                  .list(record -> new City(record.get("c").asMap())).stream();
      }
   }

}