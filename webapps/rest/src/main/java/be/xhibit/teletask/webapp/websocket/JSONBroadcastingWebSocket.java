package be.xhibit.teletask.webapp.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JSONBroadcastingWebSocket {
    private static final Collection<Session> CURRENT_CLIENTS = new ConcurrentLinkedQueue<>();

    protected static void sendAll(Object data) throws JsonProcessingException {
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

    public static int getClientCount() {
        return CURRENT_CLIENTS.size();
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
}
