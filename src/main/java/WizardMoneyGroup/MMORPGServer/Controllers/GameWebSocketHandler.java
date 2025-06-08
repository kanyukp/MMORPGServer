package WizardMoneyGroup.MMORPGServer.Controllers;

import WizardMoneyGroup.MMORPGServer.GameServer;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;import WizardMoneyGroup.MMORPGServer.Models.PlayerAction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.*;

@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST})
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>();
    private final GameServer gameServer;

//    @Autowired
//    private Executor webSocketExecutor;

//    private final BlockingQueue messageQueue = new LinkedBlockingQueue<>();


//    private final GameServer gameServer;
    @Autowired
    public GameWebSocketHandler(GameServer gameServer) {
//        this.gameServer = gameServer;
        this.gameServer = gameServer;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message ) throws Exception {
        System.out.println("Getting message");
        String payload = message.getPayload();
        System.out.println(message.getPayload());
        PlayerAction action = objectMapper.readValue(payload, PlayerAction.class);
        System.out.println(action.toString());
        gameServer.handleClientMessage(action);
    }

//    private PlayerAction parsePlayerAction(String payload){
////        gson.toJson(payload);
//        //TODO finish parsing payload
//    }

@Override
public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    Long entityId = getUserEntityId(session);
    if(entityId != null) {
        gameServer.addSession(entityId, session);
    }
    System.out.println("WebSocket connected: " + session.getId());
}
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Get player info from session before removing
        {
            Long playerId = getUserEntityId(session);
            gameServer.removeSession(playerId);
        }

        String username = session.getPrincipal().getName();
        // Remove the session from active sessions
        //gameServer.removeSession(session);
        //gameServer.removePlayer();

        // Log the disconnection
        System.out.println(String.format("WebSocket connection closed for user: %s with status: %s", username, status));

        // Optional: Notify other players about disconnection
        //String disconnectMessage = String.format("{\"type\":\"player_disconnect\",\"username\":\"%s\"}", username);
    }

    private Long getUserEntityId(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String entityIdString = uriComponents.getQueryParams().getFirst("entityId");
        try {
            return Long.parseLong(entityIdString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid entity ID received: " + entityIdString);
            return null;
        }
    }


}
