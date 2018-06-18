package micro.meet.city;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import org.reactivestreams.*;
import io.reactivex.*;
import javax.inject.*;
import java.util.stream.*;

@Controller("/city")
public class CityController {

    @Get("/echo/{text}")
    public Single<String> echo(String text) {
        return Single.just("> " + text);
    }

    @Inject CityClient client;

    @Get("/list/{count:5}")
    public Stream<City> cities(int count) {
        return client.cities(count).results.stream();
    }

    @Inject CityRepository repo;
    @Get("/named/{name}")
    public Stream<City> cities(String name) {
        return repo.findByName(name);
    }
}