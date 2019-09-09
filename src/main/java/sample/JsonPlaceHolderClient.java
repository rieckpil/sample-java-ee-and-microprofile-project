package sample;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceHolderClient {

    @GET
    @Path("/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    JsonArray getLatestComments();
}
