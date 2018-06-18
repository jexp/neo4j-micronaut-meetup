package micro.meetups;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import javax.inject.*;
import java.util.*;
import java.util.stream.*;

@Controller("/city/")
public class CityController {

    @Inject CityRepository repo;

    @Get("/{name}")
    public Stream<City> index(String name) {
        return repo.findByName(name);
    }

    @Get("country/{name}")
    public Stream<Map<String,Object>> country(String name) {
        return repo.findByCountry(name);
    }
}