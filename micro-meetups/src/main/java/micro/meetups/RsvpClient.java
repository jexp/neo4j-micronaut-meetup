package micro.meetups;

import io.micronaut.http.client.Client;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import org.reactivestreams.Publisher;

@Client("https://stream.meetup.com/2/")
public interface RsvpClient {

    @Get("/rsvps")
    public Publisher<Rsvp> index();
}