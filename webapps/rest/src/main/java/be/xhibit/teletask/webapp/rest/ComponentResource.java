package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.client.TDSClient;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final ObjectWriter WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ComponentResource.class);

    /**
     * The TDSClient object, which makes the IP socket connection to the TDS hardware.
     * The ComponentResource is loaded a a singleton.  This way we know only one instance of the TDSClient is created, and only one client is accessing
     * the TDS domotics central unit.
     */
    private final TDSClient client;

    /**
     * Constructs a new resource using the given client config.
     *
     * @param clientConfig The configuration
     */
    public ComponentResource(ClientConfigSpec clientConfig) {
        this.client = TDSClient.getInstance(clientConfig);
    }

    /**
     *
     * Gets the relay state.  Returns 0 for off, 1 for on.
     * URI: (GET) http://localhost:8080/teletask/api/relay/{number}
     *
     * @return JSON response confirming if the switch request was successful, together with the correct state.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/config")
    public Response config() throws JsonProcessingException {
        ClientConfigSpec config = this.client.getConfig();
        return Response.status(200).entity(WRITER.writeValueAsString(config)).header("Access-Control-Allow-Origin", "*").build();
    }

    /**
     *
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
        return this.getComponentState(number, Function.RELAY);
    }

    /**
     *
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
        return this.getComponentState(number, Function.COND);
    }

    /**
     *
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
        return this.getComponentState(number, Function.FLAG);
    }

    /**
     *
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
        return this.getComponentState(number, Function.MOTOR);
    }


    private Response getComponentState(int number, Function function) {
        //TODO: validate the relay to be an integer (common method), if not, return correct HTTP status code
        // Use RestEasy built-in validation: http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html/Validation.html

        //TDSComponent component = new TDSComponent(Function.RELAY, client.getRelayState(number), number);
        //APIResponse response = new APIResponse("success", component);

        // component always holds the correct state, so no need to call client.getRelayState(number)
        ComponentSpec component = this.client.getConfig().getComponent(function, number);
        component.setState(this.client.get(component));
        APIResponse response = new APIResponse("success", component);
        return Response.status(200).entity(response).header("Access-Control-Allow-Origin", "*").build();
    }

    /**
     *
     * Sets the relay state.  Returns 0 for off, 1 for on.
     * URI: (PUT) http://localhost:8080/teletask/api/relay/{number}/state/{0|1}
     *
     * @param number The relay you want to switch.
     * @param state The state you want to the relay to switch to, either 0 (off) or 1 (on) .
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/relay/{number}/state/{state}")
    public Response relay(@PathParam("number") int number, @PathParam("state") String state) {
        this.set(Function.RELAY, number, state);

        return this.buildResponse(number, Function.RELAY);
    }

    /**
     *
     * Sets the motor state.  Returns 1 for up, 0 for down.
     * URI: (PUT) http://localhost:8080/teletask/api/motor/{number}/state/{0|1}
     *
     * @param number The motor you want to switch.
     * @param state The state you want to the relay to switch to, either 0 (up) or 1 (down) .
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/motor/{number}/state/{state}")
    public Response motor(@PathParam("number") int number, @PathParam("state") String state) {
        this.set(Function.MOTOR, number, state);

        return this.buildResponse(number, Function.MOTOR);
    }

    /**
     *
     * Sets the general / local mood state.  Returns 0 for off, 1 for on.
     * URI: (PUT) http://localhost:8080/teletask/api/mood/{type}/{number}/state/{0|1}
     *
     * @param type The general / local mood type you want to switch.
     * @param number The general / local mood number you want to switch.
     * @param state The state you want to the mood to switch to, either 0 (off) or 1 (on) .
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

        this.set(function, number, state);

        return this.buildResponse(number, function);
    }


    /**
     *
     * Gets the general / local mood state.  Returns 0 for off, 1 for on.
     * URI: (GET) http://localhost:8080/teletask/api/mood/{type}/{number}/state/
     *
     * @param type The general / local mood type you want to switch.
     * @param number The general / local mood number you want to switch.
     * @return JSON response confirming if the switch request was successful.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/mood/{type}/{number}")
    public Response mood(@PathParam("type") String type, @PathParam("number") int number) {
        //TODO: validate the type to be a string (common method), if not, return correct HTTP status code
        // Use RestEasy built-in validation: http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html/Validation.html

        // no need to call, state changes are continuously monitored, and the component always holds the correct state
        /*if ("general".equals(type)) {
            client.getGeneralMoodState(number);
        } else if ("local".equals(type)) {
            client.getLocalMoodState(number);
        }*/

        Function function = null;
        if ("general".equals(type)) {
            function = Function.GENMOOD;
        } else if ("local".equals(type)) {
            function = Function.LOCMOOD;
        }

        APIResponse response = new APIResponse("success", this.client.getConfig().getComponent(function, number));
        return Response.status(200).entity(response).build();
    }

    private void set(Function function, int number, String state) {
        this.client.set(function, number, State.valueOf(state.toUpperCase()));
    }

    /**
     * Convenience mathod for creating the REST service's JSON response.
     * @param number The number of the component which has been serviced.
     * @param function The Function which has been changed.
     * @return A JSON REST response.
     */
    private Response buildResponse(int number, Function function) {
        ComponentSpec component = this.client.getConfig().getComponent(function, number);
        APIResponse apiResponse = new APIResponse("success", component);
        return Response.status(200).entity(apiResponse).header("Access-Control-Allow-Origin", "*").build();
    }

    /**
     * Helper method for determining if a parameter is an Integer
     * @param stringValue The value to check
     * @return true or false
     */
    public static boolean isInteger(String stringValue) {
        return Ints.tryParse(stringValue) != null;
    }
}
