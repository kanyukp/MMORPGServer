package WizardMoneyGroup.MMORPGServer;

import WizardMoneyGroup.MMORPGServer.Models.*;
//
import org.springframework.data.relational.core.sql.Update;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class GameServer {
    private static final int VISIBILITY_RANGE = 400;

    private final BlockingQueue<PlayerAction> actionQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService gameLoopExecutor = Executors.newScheduledThreadPool(1);
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Set<String> playersActedThisFrame = ConcurrentHashMap.newKeySet();
    private final ConcurrentLinkedQueue<Projectile> projectiles = new ConcurrentLinkedQueue<>();


    public GameServer(){
        startGameLoop();
    }

    public void handleClientMessage(PlayerAction action){
        actionQueue.offer(action);
    }

    public void receiveUpdate(PlayerAction update) {
        actionQueue.offer(update);
    }

    private void startGameLoop(){
        gameLoopExecutor.scheduleAtFixedRate(this::updateGame, 0, 1000/60, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        processActions();
        updatePlayerPositions();
    }

    private void processActions(){
        PlayerAction action;
        while ((action = actionQueue.poll()) != null) {
            if(!playersActedThisFrame.contains((action.getPlayerId()))){
                processAction(action);
                playersActedThisFrame.add(action.getPlayerId());
            }
        }
    }

    private void processAction(PlayerAction action) {
        Player player = players.get(action.getPlayerId());
        if (player != null){
            switch (action.getActionType()) {
                case MOVE:
                    movePlayer(player, action.getDirection());
                    break;
                case ATTACK:
                    createProjectile(player, action.getDirection());
                    break;
            }
        }
    }

    private void movePlayer(Player player, PlayerAction.Direction direction){
        switch (direction) {
            case UP:
                player.setY(player.getY() - 1);
                break;
            case DOWN:
                player.setY(player.getY() + 1);
                break;
            case LEFT:
                player.setX(player.getX() - 1);
                break;
            case RIGHT:
                player.setX(player.getX() + 1);
                break;
        }
    }

    private void createProjectile(Player player, PlayerAction.Direction direction){
        Projectile projectile = new Projectile(player.getX(), player.getY(), direction);
        projectiles.add(projectile);
    }

    private void updatePlayerPositions() {
        for (Player player : players.values()) {
            //DO SOMETHING TODO
        }
    }

    private void updateProjectiles() {
        for (Projectile projectile : projectiles) {
            //DO SOMETHING TODO
        }
    }

    private void sendGameStateToClients() {
        for(Player player : players.values()) {
            List<Player> visiblePlayers = getVisiblePlayers(player);
            List<Projectile> visibleProjectiles = getVisibleProjectiles(player);
        }
    }

    private List<Player> getVisiblePlayers(Player player){
        List<Player> visiblePlayers = new ArrayList<>();
        for (Player otherPlayer : players.values()){
            if(!otherPlayer.equals(player) && isWithinRange(player, otherPlayer)){
                visiblePlayers.add(otherPlayer);
            }
        }
        return visiblePlayers;
    }

    private List<Projectile> getVisibleProjectiles(Player player){
        List<Projectile> visibleProjectiles = new ArrayList<>();
        for (Projectile projectile : projectiles){
            if(isWithinRange(player, projectile)){
                visibleProjectiles.add(projectile);
            }
        }
        return visibleProjectiles;
    }

    private boolean isWithinRange(Entity entity1, Entity entity2){
        int dx = entity1.getX() - entity2.getX();
        int dy = entity1.getY() - entity2.getY();
        return dx * dx + dy * dy <= VISIBILITY_RANGE * VISIBILITY_RANGE;
    }

    private String serializeGameState(GameState gameState) {

        //TODO code in here use Jackson or GSON
        return "{}";
    }

    public void shutdown(){
        gameLoopExecutor.shutdown();
        try {
            if(!gameLoopExecutor.awaitTermination(5,TimeUnit.SECONDS)){
                gameLoopExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            gameLoopExecutor.shutdownNow();
        }
    }
}
