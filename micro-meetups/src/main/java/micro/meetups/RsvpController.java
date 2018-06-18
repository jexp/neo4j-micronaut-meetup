package micro.meetups;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.Client;
import io.micronaut.http.client.DefaultHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.sse.Event;
import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@Controller("/rsvp")
public class RsvpController {

    private final RsvpClient client;

    private final RxStreamingHttpClient rxClient;


    public RsvpController(@Client("https://stream.meetup.com/2/") RxStreamingHttpClient rxClient, RsvpClient client) {
        this.client = client;
        this.rxClient = rxClient;
    }

    @Get(value = "/", produces = MediaType.APPLICATION_JSON_STREAM)
    public org.reactivestreams.Publisher<Rsvp> index() throws MalformedURLException {
        return rxClient.
                jsonStream(HttpRequest.GET("rsvps"), Rsvp.class)
                .doOnEach(System.out::println)
                .take(5);
    }

    @Get(value = "/events", produces = MediaType.TEXT_EVENT_STREAM)
    public org.reactivestreams.Publisher<Event<Rsvp>> events() throws MalformedURLException {
        return rxClient.
                jsonStream(HttpRequest.GET("rsvps"), Rsvp.class)
                .doOnEach(System.out::println)
                .map(Event::of)
                .take(5);
    }

    @Get("/2")
    public org.reactivestreams.Publisher<Rsvp> index2() {
        Processor<Rsvp,Rsvp> handler = new Processor<Rsvp,Rsvp>() {
            private Subscriber<? super Rsvp> downstream;
            int max = 100;
            private Subscription upstream;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.upstream = subscription;
            }

            @Override
            public void onNext(Rsvp rsvp) {
                System.out.println(rsvp);
                if (max-- == 0) {
                    upstream.cancel();
                } else {
                    // upstream.request(1);
                }
                downstream.onNext(rsvp);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                upstream.cancel();
                downstream.onError(t);
            }

            @Override
            public void onComplete() {
                System.out.println("Complete");
                downstream.onComplete();
            }

            @Override
            public void subscribe(Subscriber<? super Rsvp> subscriber) {
                this.downstream = subscriber;
                downstream.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        upstream.request(n);
                    }

                    @Override
                    public void cancel() {
                        upstream.cancel();
                    }
                });
            }
        };
        client.index().subscribe(handler);
        return handler;
    }

    @Get("/test")
    public Publisher<Event<String>> sse() {
        String[] versions = new String[]{"1.0", "2.0"};

        return Flowable.generate(() -> 0, (i, emitter) -> {
            if (i < versions.length) {
                emitter.onNext(
                        Event.<String>of("Micronaut " + versions[i] + " Released. Come and get it")
                );
            } else {
                emitter.onComplete();
            }
            return ++i;
        });
    }
}
