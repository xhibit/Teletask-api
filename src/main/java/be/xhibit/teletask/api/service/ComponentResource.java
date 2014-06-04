package be.xhibit.teletask.api.service;

import be.xhibit.teletask.api.model.APIResponse;
import be.xhibit.teletask.api.model.TDSComponent;
import be.xhibit.teletask.api.enums.Function;
import be.xhibit.teletask.api.model.TDSClientConfig;
import be.xhibit.teletask.client.TDSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * The ComponentResource lists all REST API methods available for the Teletask service.
 */
@Path("/")
public class ComponentResource {

    static final Logger logger = LogManager.getLogger(ComponentResource.class.getName());

    /**
     * The TDSClient object, which makes the IP socket connection to the TDS hardware.
     * The ComponentResource is loaded a a singleton.  This way we know only one instance of the TDSClient is created, and only one client is accessing
     * the TDS domotics central unit.
     */
    private TDSClient client;

    /**
     * Default constructor.
     */
    public ComponentResource() {
        client = TDSClient.getInstance();
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
        //TODO: validate the relay to be an integer (common method), if not, return correct HTTP status code
        // Use RestEasy built-in validation: http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html/Validation.html

        //TDSComponent component = new TDSComponent(Function.RELAY, client.getRelayState(number), number);
        //APIResponse response = new APIResponse("success", component);

        // component always holds the correct state, so no need to call client.getRelayState(number)
        APIResponse response = new APIResponse("success", client.getComponent(Function.RELAY, number));
        return Response.status(200).entity(response).build();
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
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/relay/{number}/state/{state}")
    public Response relay(@PathParam("number") int number, @PathParam("state") int state) {
        //TODO: validate the relay to be an integer (common method), if not, return correct HTTP status code
        //TODO: validate the status to be an integer (common method) and either value 0 or 1, if not, return correct HTTP status code(s)
        // Use RestEasy built-in validation: http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html/Validation.html

        if (state == 1) {
            client.switchRelayOn(number);
        } else {
            client.switchRelayOff(number);
        }

        return this.buildResponse(number, state, Function.RELAY);
    }

    /**
     *
     * Sets the general / local mood state.  Returns 0 for off, 1 for on.
     * URI: (PUT) http://localhost:8080/teletask/api/mood/{type}/{number}/state/{0|1}
     *
     * @param number The general / local mood you want to switch.
     * @param state The state you want to the mood to switch to, either 0 (off) or 1 (on) .
     * @return JSON response confirming if the switch request was successful.
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/mood/{type}/{number}/state/{state}")
    public Response mood(@PathParam("type") String type, @PathParam("number") int number, @PathParam("state") int state) {
        //TODO: validate the type to be a string (common method), if not, return correct HTTP status code
        //TODO: validate the status to be an integer (common method) and either value 0 or 1, if not, return correct HTTP status code(s)
        // Use RestEasy built-in validation: http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html/Validation.html

        if (state == 1) {
            if ("general".equals(type)) {
                client.switchGeneralMoodOn(number);
            } else if ("local".equals(type)) {
                client.switchLocalMoodOn(number);
            }
        } else {
            if ("general".equals(type)) {
                client.switchGeneralMoodOff(number);
            } else if ("local".equals(type)) {
                client.switchLocalMoodOff(number);
            }
        }

        Function function = null;
        if ("general".equals(type)) {
            function = Function.GENMOOD;
        } else if ("local".equals(type)) {
            function = Function.LOCMOOD;
        }

        return this.buildResponse(number, state, function);
    }

    /**
     * Convenience mathod for creating the REST service's JSON response.
     * @param number The number of the component which has been serviced.
     * @param state The current state of the component.
     * @param function The Function which has been changed.
     * @return A JSON REST response.
     */
    private Response buildResponse(int number, int state, Function function) {
        TDSComponent component = new TDSComponent(function, state, number);
        APIResponse apiResponse = new APIResponse("success", component);
        return Response.status(200).entity(apiResponse).build();
    }

    /**
     * Helper method for determining if a parameter is an Integer
     * @param stringValue The value to check
     * @return true or false
     */
    public static boolean isInteger(String stringValue) {
        try {
            Integer.parseInt(stringValue);
        } catch(NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
