package sample;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.stream.JsonCollectors;

@ApplicationScoped
public class SampleService {

    @Inject
    @ConfigProperty(name = "message")
    private String message;

    @Inject
    @RestClient
    private JsonPlaceHolderClient jsonPlaceHolderClient;

    @PostConstruct
    public void init() {
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
        return this.jsonPlaceHolderClient
                .getLatestComments()
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
