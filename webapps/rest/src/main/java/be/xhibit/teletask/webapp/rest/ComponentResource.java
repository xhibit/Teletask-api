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

    /**
     * Gets the relay state.  Returns 0 for off, 1 for on.
     * URI: (GET) http://localhost:8080/teletask/api/relay/{number}
     *
     * @param number The relay you want to query the state for.
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/relay/{number}")
    public Response relay(@PathParam("number") int number) {
        return this.get(number, Function.RELAY);
    }

    /**
     * Gets the condition state.  Returns 1 for true, 0 for false.
     * URI: (GET) http://localhost:8080/teletask/api/condition/{number}
     *
     * @param number The condition you want to query the state for.
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/condition/{number}")
    public Response condition(@PathParam("number") int number) {
        return this.get(number, Function.COND);
    }

    /**
     * Gets the flag state.  Returns 1 for true, 0 for false.
     * URI: (GET) http://localhost:8080/teletask/api/flag/{number}
     *
     * @param number The flag you want to query the state for.
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/flag/{number}")
    public Response flag(@PathParam("number") int number) {
        return this.get(number, Function.FLAG);
    }

    /**
     * Gets the motor state.  Returns 1 for up, 0 for down.
     * URI: (GET) http://localhost:8080/teletask/api/motor/{number}
     *
     * @param number The motor you want to query the state for.
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/motor/{number}")
    public Response motor(@PathParam("number") int number) {
        return this.get(number, Function.MOTOR);
    }

    /**
     * Sets the relay state.  Returns 0 for off, 1 for on.
     * URI: (PUT) http://localhost:8080/teletask/api/relay/{number}/state/{0|1}
     *
     * @param number The relay you want to switch.
     * @param state  The state you want to the relay to switch to, either 0 (off) or 1 (on) .
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/relay/{number}/state/{state}")
    public Response relay(@PathParam("number") int number, @PathParam("state") String state) {
        return this.set(Function.RELAY, number, state);
    }

    /**
     * Sets the motor state.  Returns 1 for up, 0 for down.
     * URI: (PUT) http://localhost:8080/teletask/api/motor/{number}/state/{0|1}
     *
     * @param number The motor you want to switch.
     * @param state  The state you want to the relay to switch to, either 0 (up) or 1 (down) .
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/motor/{number}/state/{state}")
    public Response motor(@PathParam("number") int number, @PathParam("state") String state) {
        return this.set(Function.MOTOR, number, state);
    }

    /**
     * Sets the general / local mood state.  Returns 0 for off, 1 for on.
     * URI: (PUT) http://localhost:8080/teletask/api/mood/{type}/{number}/state/{0|1}
     *
     * @param type   The general / local mood type you want to switch.
     * @param number The general / local mood number you want to switch.
     * @param state  The state you want to the mood to switch to, either 0 (off) or 1 (on) .
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/mood/{type}/{number}/state/{state}")
    public Response mood(@PathParam("type") String type, @PathParam("number") int number, @PathParam("state") String state) {
        Function function = null;
        if ("general".equals(type)) {
            function = Function.GENMOOD;
        } else if ("local".equals(type)) {
            function = Function.LOCMOOD;
        }

        return this.set(function, number, state);
    }


    /**
     * Gets the general / local mood state.  Returns 0 for off, 1 for on.
     * URI: (GET) http://localhost:8080/teletask/api/mood/{type}/{number}/state/
     *
     * @param type   The general / local mood type you want to switch.
     * @param number The general / local mood number you want to switch.
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/mood/{type}/{number}")
    public Response mood(@PathParam("type") String type, @PathParam("number") int number) {
        Function function = null;
        if ("general".equals(type)) {
            function = Function.GENMOOD;
        } else if ("local".equals(type)) {
            function = Function.LOCMOOD;
        }

        return this.get(number, function);
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
    @Path("/component/{function}/{number}/state/{state}")
    public Response component(@PathParam("function") String function, @PathParam("number") int number, @PathParam("state") String state) {
        Function functionEnum = Function.valueOf(function.toUpperCase());

        return this.set(functionEnum, number, state);
    }

    private Response get(int number, Function function) {
        return this.buildGetResponse(this.getClient().getConfig().getComponent(function, number));
    }

    private Response set(Function function, int number, String state) {
        this.getClient().set(function, number, function.stateValue(state));

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
