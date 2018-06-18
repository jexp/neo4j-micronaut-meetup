package micro.meet.city;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

@Singleton
public class CityMapRepository implements CityRepository {
   private final Map<Long,City> data=new ConcurrentHashMap<>();

   public void save(City c) {
      data.putIfAbsent(c.id, c);
   } 
   public Stream<City> findByName(String name) {
      return data.values().stream().filter(c -> c.city.contains(name));
   }
}