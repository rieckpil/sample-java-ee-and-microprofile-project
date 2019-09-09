package sample;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.stream.JsonCollectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SampleService {

    @Inject
    @ConfigProperty(name = "message")
    private String message;

    private WebTarget webTarget;

    @PostConstruct
    public void init() {

        Client client = ClientBuilder
                .newBuilder()
                .readTimeout(2, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();

        this.webTarget = client.target("https://jsonplaceholder.typicode.com/comments");

        System.out.println("SampleService is initialized");
    }

    @Counted
    public String getSecretMessage() {
        return message;
    }

    @Timed
    @Fallback(fallbackMethod = "getDefaultComments")
    @Retry(maxRetries = 5, maxDuration = 5000, delay = 200, jitter = 100)
    public JsonArray getLatestComments() {
        return this.webTarget
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()
                .readEntity(JsonArray.class)
                .stream()
                .limit(5)
                .collect(JsonCollectors.toJsonArray());
    }

    public JsonArray getDefaultComments() {
        return Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("postId", 1)
                        .add("id", 1)
                        .add("name", "duke")
                        .add("email", "duke@java.ee")
                        .add("body", "Nice message!")
                        .build())
                .build();
    }
}
