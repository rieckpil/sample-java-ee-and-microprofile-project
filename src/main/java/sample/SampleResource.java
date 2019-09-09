package sample;

import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("sample")
@Produces(MediaType.APPLICATION_JSON)
public class SampleResource {

    @Inject
    private SampleService sampleService;

    @GET
    @Metered
    public Response message() {

        JsonObject result = Json.createObjectBuilder()
                .add("id", 42)
                .add("message", sampleService.getSecretMessage())
                .add("comments", sampleService.getLatestComments())
                .build();

        return Response.ok(result).build();
    }

}
