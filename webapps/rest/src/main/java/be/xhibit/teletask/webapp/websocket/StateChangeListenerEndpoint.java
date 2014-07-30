package be.xhibit.teletask.webapp.websocket;

import be.xhibit.teletask.client.listener.StateChangeListener;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.webapp.ClientHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint("/state-changes")
public class StateChangeListenerEndpoint {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(StateChangeListenerEndpoint.class);

    private static final Collection<Session> CURRENT_CLIENTS = new ConcurrentLinkedQueue<>();

    static {
        ClientHolder.getClient().registerStateChangeListener(new StateChangeListener() {
            @Override
            public void event(List<ComponentSpec> components) {
                try {
                    sendAll(components);
//                    sendAll(ClientHolder.getClient().getConfig());
                } catch (JsonProcessingException e) {
                    LOG.error("Exception ({}) caught in event: {}", e.getClass().getName(), e.getMessage(), e);
                }
            }
        });
    }

    private static class Data {
        private final List<ComponentSpec> components;

        private Data(List<ComponentSpec> components) {
            this.components = components;
        }

        public List<ComponentSpec> getComponents() {
            return this.components;
        }
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
    }

    @OnOpen
    public void open(Session session) {
        CURRENT_CLIENTS.add(session);
    }

    @OnError
    public void error(Session session, Throwable t) {
        CURRENT_CLIENTS.remove(session);
    }

    @OnClose
    public void closedConnection(Session session) {
        CURRENT_CLIENTS.remove(session);
    }

    private static void sendAll(Object data) throws JsonProcessingException {
        String msg = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(data);
        try {
            Collection<Session> closedSessions = new ArrayList<>();
            for (Session session : CURRENT_CLIENTS) {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            CURRENT_CLIENTS.removeAll(closedSessions);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}