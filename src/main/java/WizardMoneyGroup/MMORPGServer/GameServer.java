package WizardMoneyGroup.MMORPGServer;

import WizardMoneyGroup.MMORPGServer.Models.*;
//
import WizardMoneyGroup.MMORPGServer.Services.InventoryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.*;

@Component
public class GameServer {
    private static final int VISIBILITY_RANGE = 400;
    private static final int BREAK_TIME_MS = 2000;
    private static final int BREAK_RANGE = 50;

    private QuadTree quadTree;
    private final Map<Long, Player> players = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Projectile> projectiles = new ConcurrentLinkedQueue<>();
    private List<Block> blocks;
    private List<ItemEntity> itemEntities;
    private final InventoryService inventoryService;
    private List<WebSocketSession> sessions = new ArrayList<>();
    private final BlockingQueue<PlayerAction> actionQueue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService executorService;
    private Map<Long, ScheduledFuture<?>> breakingTasks;
    private Gson gson = new Gson();


    private final ScheduledExecutorService gameLoopExecutor = Executors.newScheduledThreadPool(1);

    private final Set<Long> playersActedThisFrame = ConcurrentHashMap.newKeySet();

        @Autowired
        public GameServer(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
            this.quadTree = new QuadTree(0,0, 0, 10000, 10000); // adjust dimensions as needed
            this.blocks = new ArrayList<>();
            this.itemEntities = new ArrayList<>();
            this.executorService = Executors.newScheduledThreadPool(1);
            this.breakingTasks = new ConcurrentHashMap<>();
//            System.out.println("In GameServer: Constructor");
            try {
                startGameLoop();
            } catch (Exception e) {
                System.err.println("GameServer failed to start: " + e.getMessage());
                e.printStackTrace();
            }
        }

    public void handleClientMessage(PlayerAction action) {
        System.out.println("In GameServer: handleClientMessage");
        if (!players.containsKey(action.getPlayerId()))
        {

        }
        actionQueue.offer(action);
    }

    public void receiveUpdate(PlayerAction update) {
        actionQueue.offer(update);
    }

    private void startGameLoop() {
//        System.out.println("In GameServer: startGameLoop");
        gameLoopExecutor.scheduleAtFixedRate(this::updateGame, 0, 1000 / 60, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        playersActedThisFrame.clear();
        quadTree.clear();
        for (Player player : players.values()) {
            System.out.println("In GameServer: updateGame: addPlayers to quadTree");
            quadTree.insert(player);
        }
        for (Projectile projectile : projectiles) {
            quadTree.insert(projectile);
        }
        for (Block block : blocks) {
            quadTree.insert(block);
        }
        for (ItemEntity itemEntity : itemEntities) {
            quadTree.insert(itemEntity);
        }
        System.out.println("In GameServer: updateGame: post quadTree.insert");
        processActions();
        updateProjectiles();
        updatePlayerPositions();
        checkItemCollisions();
        //SAVE STATE TO DB TODO
        sendGameStateToClients();
    }

    private void processActions() {
        System.out.println("In GameServer: processActions");
        PlayerAction action;
        while ((action = actionQueue.poll()) != null) {
            System.out.println("In GameServer: processActions: whileLoop");
            if (!playersActedThisFrame.contains((action.getPlayerId()))) {
                processAction(action);
                playersActedThisFrame.add(action.getPlayerId());
            }
        }
    }


    private void processAction(PlayerAction action) {
        System.out.println("In GameServer: processAction");
        Player player = players.get(action.getPlayerId());
        if (player != null) {
            if (player.isInventoryOpen() && action.getActionType() != PlayerAction.ActionType.CLOSE_INVENTORY) {
                return;
            }

            switch (action.getActionType()) {
                case MOVE:
                    movePlayer(player, action.getDirection());
                    player.setCurrentAction(Player.Action.MOVE);
                    break;
                case ATTACK:
                    createProjectile(player, action.getDirection());
                    player.setCurrentAction(Player.Action.ATTACK);
                    break;
                case PLACE:
                    placeBlock(player, action);
                    player.setCurrentAction(Player.Action.PLACE);
                    break;
                case BREAK:
                    breakBlock(player, action);
                    player.setCurrentAction(Player.Action.BREAK);
                    break;
                case DROP:
                    dropItem(player, action);
                    player.setCurrentAction(Player.Action.IDLE);
                    break;
                case OPEN_INVENTORY:
                    openInventory(player);
                    //player.setCurrentAction(Player.Action.ATTACK)
                    break;
                case CLOSE_INVENTORY:
                    closeInventory(player);
                    //player.setCurrentAction(Player.Action.ATTACK)
                    break;
            }
        }
    }

    private void movePlayer(Player player, PlayerAction.Direction direction) {
        System.out.println("In GameServer: movePlayer");

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

    private void createProjectile(Player player, PlayerAction.Direction direction) {
        Projectile projectile = new Projectile(player.getX(), player.getY(), direction);
        projectiles.add(projectile);
    }

    private void placeBlock(Player player, PlayerAction action) {
        int x = action.getX();
        int y = action.getY();
        int width = action.getWidth();
        int height = action.getHeight();
        String sprite = action.getSprite();

        switch (action.getDirection()) {
            case UP:
                y -= height;
                break;
            case DOWN:
                y += player.getHeight();
                break;
            case LEFT:
                x -= width;
                break;
            case RIGHT:
                x += player.getWidth();
                break;
        }

        Block tempBlock = new Block(x, y, width, height, true, sprite);

        List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), tempBlock);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Block && ((Block) entity).isCollide()) {
                if (intersects(tempBlock, entity)) {
//                    sendResponseToPlayer(player, "No Space"); TODO
                    return;
                }
            }
        }

        blocks.add(tempBlock);

    }

    private void breakBlock(Player player, PlayerAction action) {
        int mouseX = action.getX();
        int mouseY = action.getY();

        List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), player);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Block && ((Block) entity).isCollide()) {
                Block block = (Block) entity;
                Block mouse = new Block(mouseX, mouseY);
                if (intersects(block, mouse) && isWithinBreakRange(player, block)) {
                    initiateBreakingProcess(player, block);
                    return;
                }
            }
        }
    }

    private void initiateBreakingProcess(Player player, Block block) {

        ScheduledFuture<?> existingTask = breakingTasks.get(player.getId());
        if (existingTask != null) {
            existingTask.cancel(true);
        }

        ScheduledFuture<?> breakingTask = executorService.schedule(() -> {
            if (blocks.contains(block) && isWithinBreakRange(player, block)) {
                blocks.remove(block);
                //TODO breaking successful
            } else {
                //TODO breaking failed
            }
        }, BREAK_TIME_MS, TimeUnit.MILLISECONDS);
        breakingTasks.put(player.getId(), breakingTask);
    }

    private void dropItem(Player player, PlayerAction action) {
        ItemEntity itemEntity = new ItemEntity(action.getX(), action.getY(), action.getWidth(), action.getHeight(), action.getItemName(), action.getSprite());
        itemEntities.add(itemEntity);

        Item itemToDrop = player.getInventory().stream()
                .filter(item -> item.getName().equals(action.getItemName()))
                .findFirst().orElse(null);
        if (itemToDrop != null) {
            inventoryService.removeItemFromPlayer(player, itemToDrop);
        }
    }

    private void checkItemCollisions() {
        System.out.println("In GameServer: checkItemCollisions");

        Iterator<ItemEntity> iterator = itemEntities.iterator();
        while (iterator.hasNext()) {
            ItemEntity itemEntity = iterator.next();
            List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), itemEntity);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (intersects(itemEntity, player)) {
                        Item item = new Item(0, itemEntity.getName(), itemEntity.getSprite());
                        inventoryService.addItemToPlayer(player, item);
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private void openInventory(Player player) {
        player.setInventoryOpen(true);
        List<Item> inventory = player.getInventory();
        gson.toJson(inventory);
        //TODO send inventory to client
    }

    private void closeInventory(Player player) {
        player.setInventoryOpen(false);
    }

    private void updatePlayerPositions() {
        System.out.println("In GameServer: checkItemCollisions");
        for (Player player : players.values()) {
            //DO SOMETHING TODO
        }
    }

    private void updateProjectiles() {

        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.move();

            List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), projectile);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Block && ((Block) entity).isCollide()) {
                    if (intersects(projectile, entity)) {
                        //TODO destroy projectile
                        iterator.remove();
                        break;
                    }
                } else if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (intersects(projectile, player)) {
                        player.setHp(player.getHp() - projectile.getDamage());
                        //TODO destroy projectile and maybe player?
                        iterator.remove();
                        break;
                    }
                }
            }
            if (projectile.hasExceededMaxDistance()) {
                iterator.remove();
                //TODO destroy the projectile
            }
        }
    }

    private boolean intersects(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    private void sendGameStateToClients() {
        System.out.println("In GameServer: sendGameStateToClients");
        GameState gameState = new GameState(players.values(), projectiles, blocks, itemEntities);
        for (Player player : players.values()) {
            List<Player> visiblePlayers = getVisiblePlayers(player);
            List<Projectile> visibleProjectiles = getVisibleProjectiles(player);
        }
    }

    private List<Player> getVisiblePlayers(Player player) {
        System.out.println("In GameServer: getVisiblePlayers");

        List<Player> visiblePlayers = new ArrayList<>();
        for (Player otherPlayer : players.values()) {
            if (!otherPlayer.equals(player) && isWithinVisibilityRange(player, otherPlayer)) {
                visiblePlayers.add(otherPlayer);
            }
        }
        return visiblePlayers;
    }

    private List<Projectile> getVisibleProjectiles(Player player) {
        System.out.println("In GameServer: getVisibleProjectiles");

        List<Projectile> visibleProjectiles = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (isWithinVisibilityRange(player, projectile)) {
                visibleProjectiles.add(projectile);
            }
        }
        return visibleProjectiles;
    }

    private boolean isWithinVisibilityRange(Entity entity1, Entity entity2) {
        System.out.println("In GameServer: isWithinVisibilityRange");

        int dx = entity1.getX() - entity2.getX();
        int dy = entity1.getY() - entity2.getY();
        return dx * dx + dy * dy <= VISIBILITY_RANGE * VISIBILITY_RANGE;
    }

    private boolean isWithinBreakRange(Entity entity1, Entity entity2) {
        int dx = entity1.getX() - entity2.getX();
        int dy = entity1.getY() - entity2.getY();
        return dx * dx + dy * dy <= BREAK_RANGE * BREAK_RANGE;
    }

    private String serializeGameState(GameState gameState) {
        System.out.println("In GameServer: serializeGameState");

        return gson.toJson(gameState);
    }

    public void shutdown() {
        gameLoopExecutor.shutdown();
        try {
            if (!gameLoopExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                gameLoopExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            gameLoopExecutor.shutdownNow();
        }
    }

    public void removeSession( WebSocketSession session){
        sessions.remove(session);
    }

    public void addSession( WebSocketSession session){
        sessions.add(session);
    }

    public void addPlayer(Player player) {
            players.put(player.getId(), player);
    }
}
