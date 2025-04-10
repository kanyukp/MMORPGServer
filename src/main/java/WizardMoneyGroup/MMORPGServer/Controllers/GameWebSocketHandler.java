package WizardMoneyGroup.MMORPGServer.Controllers;

import WizardMoneyGroup.MMORPGServer.GameServer;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import WizardMoneyGroup.MMORPGServer.Models.PlayerAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.*;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>();

    @Autowired
    private Executor webSocketExecutor;

    private final BlockingQueue messageQueue = new LinkedBlockingQueue<>();


//    private final GameServer gameServer;

    public GameWebSocketHandler() {
//        this.gameServer = gameServer;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message ) throws Exception {
        String payload = message.getPayload();
//        PlayerAction action = parsePlayerAction(payload);
//        gameServer.handleClientMessage(action);
    }

//    private PlayerAction parsePlayerAction(String payload){
////        gson.toJson(payload);
//        //TODO finish parsing payload
//    }

//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        scheduler.scheduleAtFixedRate( () -> {
//            try {
//                UpdateInput message = new UpdateInput();
//                message.setUsername("");
//            }
//        })
//    }

}
