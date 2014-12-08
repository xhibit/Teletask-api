package be.xhibit.teletask.webapp.rest;

import javax.ws.rs.core.Response;

public abstract class ResourceSupport {
    protected Response buildSuccessResponse(Object response) {
        return Response.status(200).entity(response).header("Access-Control-Allow-Origin", "*").build();
    }

    protected Response buildNotAuthorizedResponse() {
        return Response.status(403).entity("Not Authorized").build();
    }
}
