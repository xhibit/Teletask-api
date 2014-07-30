package be.xhibit.teletask.client.builder.message.response;

import be.xhibit.teletask.client.builder.message.EventMessage;

public class EventMessageServerResponse implements ServerResponse {
    private final EventMessage eventMessage;

    public EventMessageServerResponse(EventMessage eventMessage) {
        this.eventMessage = eventMessage;
    }

    public EventMessage getEventMessage() {
        return this.eventMessage;
    }
}
