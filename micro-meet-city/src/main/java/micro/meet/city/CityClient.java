package micro.meet.city;

import io.micronaut.http.client.Client;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;

@Client("https://api.meetup.com/2")
public interface CityClient {

    @Get("/cities{?page}")
    public CityResult cities(int page);
}