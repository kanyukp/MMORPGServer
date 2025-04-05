package WizardMoneyGroup.MMORPGServer;

import WizardMoneyGroup.MMORPGServer.Models.GameData;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import WizardMoneyGroup.MMORPGServer.Models.UpdateInput;
import org.springframework.data.relational.core.sql.Update;

import java.util.Map;
import java.util.concurrent.*;

public class GameServer {
    private final BlockingQueue<UpdateInput> updateQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService gameLoopExecutor = Executors.newScheduledThreadPool(1);
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public GameServer(){
        startGameLoop();
    }

    public void receiveUpdate(UpdateInput update) {
        updateQueue.offer(update);
    }

    private void startGameLoop(){
        gameLoopExecutor.scheduleAtFixedRate(this::updateGame, 0, 1000/60, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        processUpdates();
        updatePlayerPositions();
    }

    private void processUpdates(){
        UpdateInput update;
        while ((update = updateQueue.poll()) != null) {
            processUpdate(update);
        }
    }

    private void processUpdate(UpdateInput update) {
        Player player = players.get(update.getUsername());
        if (player != null){
            player.setXpos(player.getXpos() + 2);
        }
    }


}
