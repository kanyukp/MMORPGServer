package WizardMoneyGroup.MMORPGServer.Controllers;

import WizardMoneyGroup.MMORPGServer.GameServer;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;import WizardMoneyGroup.MMORPGServer.Models.PlayerAction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    public GameWebSocketHandler(GameServer gameServer) {
//        this.gameServer = gameServer;
        this.gameServer = gameServer;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message ) throws Exception {
        String payload = message.getPayload();
        PlayerAction action = objectMapper.readValue(payload, PlayerAction.class);
        gameServer.handleClientMessage(action);
    }

//    private PlayerAction parsePlayerAction(String payload){
////        gson.toJson(payload);
//        //TODO finish parsing payload
//    }

@Override
public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("WebSocket connected: " + session.getId());
    gameServer.addSession(session);
}

}
