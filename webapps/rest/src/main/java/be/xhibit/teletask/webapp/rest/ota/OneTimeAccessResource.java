package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.webapp.ClientHolder;
import be.xhibit.teletask.webapp.rest.ResourceSupport;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ota")
public class OneTimeAccessResource extends ResourceSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(OneTimeAccessResource.class);

    private OneTimeAccessTokenStore oneTimeAccessTokenStore;
    private final String otaApiKey;

    public OneTimeAccessResource() {
        this.otaApiKey = System.getProperty("ota.key", new UUIDGenerator().generateId(this).toString());
        LOG.info("One Time Access API key: '{}'", this.otaApiKey);
        this.registerOneTimeAccessTokenStore();
    }

    private void registerOneTimeAccessTokenStore() {
        String otaImpl = System.getProperty("ota.impl", "be.xhibit.teletask.webapp.rest.ota.OneTimeAccessTokenStoreMemoryImpl");
        try {
            this.oneTimeAccessTokenStore = (OneTimeAccessTokenStore) Class.forName(otaImpl).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate/{apiKey}/{function}/{number}/{state}")
    public Response generate(@PathParam("apiKey") String apiKey, @PathParam("function") String function, @PathParam("number") int number, @PathParam("state") String state) {
        OneTimeAccessToken response = OneTimeAccessResource.this.getOneTimeAccessTokenStore().generate(Function.valueOf(function.toUpperCase()), number, state);
        return this.handle(apiKey, response);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/execute/{token}")
    public Response execute(@PathParam("token") String tokenId) {
        OneTimeAccessExecutionResult response = null;
        OneTimeAccessToken token = this.getOneTimeAccessTokenStore().get(tokenId);
        if (token != null && token.isValid()) {
            try {
                ClientHolder.getClient().set(token.getFunction(), token.getNumber(), token.getState());
                response = this.createExecutionResult(token, "OK");
            } catch (Exception e) {
                response = this.createExecutionResult(token, e.getMessage());
            }
        } else {
            response = new OneTimeAccessExecutionResult(null, "Invalid Token");
        }

        return this.buildSuccessResponse(response);
    }

    private OneTimeAccessExecutionResult createExecutionResult(OneTimeAccessToken token, String result) {
        OneTimeAccessExecutionResult response;
        response = new OneTimeAccessExecutionResult(ClientHolder.getClient().getConfig().getComponent(token.getFunction(), token.getNumber()), result);
        return response;
    }

    private Response handle(String apiKey, Object responseObject) {
        Response response = null;
        if (this.otaApiKey.equals(apiKey)) {
            response = this.buildSuccessResponse(responseObject);
        } else {
            response = this.buildNotAuthorizedResponse();
        }
        return response;
    }

    public OneTimeAccessTokenStore getOneTimeAccessTokenStore() {
        return this.oneTimeAccessTokenStore;
    }
}
