package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.client.TeletaskClient;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.webapp.ClientHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The ComponentResource lists all REST API methods available for the Teletask service.
 */
@Path("/")
public class ComponentResource {
    private static final ObjectWriter PRETTY_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();
    private static final ObjectWriter WRITER = new ObjectMapper().writer();

    /**
     * The TDSClient object, which makes the IP socket connection to the TDS hardware.
     * The ComponentResource is loaded a a singleton.  This way we know only one instance of the TDSClient is created, and only one client is accessing
     * the TDS domotics central unit.
     */
    private TeletaskClient client;

    public ComponentResource() {
    }

    /**
     * Gets the complete config in JSON.
     * URI: (GET) http://localhost:8080/teletask/api/config
     *
     * @return JSON representation of the complete Teletask config in place..
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/config")
    public Response config() throws JsonProcessingException {
        return this.buildsuccessResponse(WRITER.writeValueAsString(this.getClient().getConfig()));
    }

    /**
     * Gets the complete config in JSON.
     * URI: (GET) http://localhost:8080/teletask/api/pretty-config
     *
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pretty-config")
    public Response prettyConfig() throws JsonProcessingException {
        return this.buildsuccessResponse(PRETTY_WRITER.writeValueAsString(this.getClient().getConfig()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/component/{function}/{number}")
    public Response component(@PathParam("function") String function, @PathParam("number") int number) {
        APIResponse response = new APIResponse("success", this.getClient().getConfig().getComponent(Function.valueOf(function.toUpperCase()), number));
        return this.buildsuccessResponse(response);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/refresh/{function}/{number}")
    public Response refresh(@PathParam("function") String function, @PathParam("number") int number) {
        ComponentSpec component = this.getClient().getConfig().getComponent(Function.valueOf(function.toUpperCase()), number);
        this.getClient().get(component);
        APIResponse response = new APIResponse("success", component);
        return this.buildsuccessResponse(response);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/component/{function}/{number}/state/{state}")
    public Response component(@PathParam("function") String function, @PathParam("number") int number, @PathParam("state") String state) {
        Function functionEnum = Function.valueOf(function.toUpperCase());

        return this.set(functionEnum, number, state);
    }

    private Response get(int number, Function function) {
        return this.buildGetResponse(this.getClient().getConfig().getComponent(function, number));
    }

    private Response set(Function function, int number, String state) {
        this.getClient().set(function, number, state);

        return this.buildGetResponse(this.getClient().getConfig().getComponent(function, number));
    }

    private Response buildGetResponse(ComponentSpec... component) {
        APIResponse apiResponse = new APIResponse("success", component);
        return Response.status(200).entity(apiResponse).header("Access-Control-Allow-Origin", "*").build();
    }

    private Response buildsuccessResponse(Object response) {
        return Response.status(200).entity(response).header("Access-Control-Allow-Origin", "*").build();
    }

    private TeletaskClient getClient() {
        while (this.client == null) {
            this.setClient(ClientHolder.getClient());
        }
        return this.client;
    }

    private void setClient(TeletaskClient client) {
        this.client = client;
    }
}
