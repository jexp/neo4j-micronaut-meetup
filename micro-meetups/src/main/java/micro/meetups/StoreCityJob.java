package micro.meetups;

import javax.inject.*;
import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class StoreCityJob {

    @Inject CityClient client;
    @Inject CityRepository repo;

    @Scheduled(fixedRate = "5m")
    public void process() {
        repo.save(client.cities(5).results);
    }
}