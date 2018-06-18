package micro.meet.city;

import javax.inject.*;
import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class CityJob {

    @Inject CityClient client;
    @Inject CityRepository repo;

    @Scheduled(fixedRate = "5m")
    public void process() {
        client.cities(5).results.forEach(repo::save);
    }
}