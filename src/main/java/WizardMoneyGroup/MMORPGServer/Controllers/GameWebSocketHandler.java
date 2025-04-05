package WizardMoneyGroup.MMORPGServer.Controllers;

import WizardMoneyGroup.MMORPGServer.Models.GameData;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import WizardMoneyGroup.MMORPGServer.Models.UpdateInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>()

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        scheduler.scheduleAtFixedRate( () -> {
            try {
                UpdateInput message = new UpdateInput();
                message.setUsername("");
            }
        })
    }
}
