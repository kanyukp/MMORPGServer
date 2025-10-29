package WizardMoneyGroup.MMORPGServer;

import WizardMoneyGroup.MMORPGServer.Models.*;
//
import WizardMoneyGroup.MMORPGServer.Services.BlockService;
import WizardMoneyGroup.MMORPGServer.Services.InventoryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

@Component
public class GameServer {
    private static final int VISIBILITY_RANGE = 200;
    private static final int BREAK_TIME_MS = 2000;
    private static final int BREAK_RANGE = 50;

    private QuadTree quadTree;
    private final Map<Long, Player> players = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Projectile> projectiles = new ConcurrentLinkedQueue<>();
    private List<Block> blocks;
    private List<ItemEntity> itemEntities;
    private final InventoryService inventoryService;
    private final BlockService blockService;
    private Map<Long, WebSocketSession> sessions = new HashMap<>();
    private final BlockingQueue<PlayerAction> actionQueue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService executorService;
    private Map<Long, ScheduledFuture<?>> breakingTasks;
    private Gson gson = new Gson();


    private final ScheduledExecutorService gameLoopExecutor = Executors.newScheduledThreadPool(1);

    private final Set<Long> playersActedThisFrame = ConcurrentHashMap.newKeySet();

        @Autowired
        public GameServer(InventoryService inventoryService, BlockService blockService) {
            this.inventoryService = inventoryService;
            this.blockService = blockService;
            this.quadTree = new QuadTree(0,0, 0, 10000, 10000); // adjust dimensions as needed
            this.blocks = new ArrayList<>();
            this.itemEntities = new ArrayList<>();
            this.executorService = Executors.newScheduledThreadPool(1);
            this.breakingTasks = new ConcurrentHashMap<>();

            try {
                startGameLoop();
            } catch (Exception e) {
                System.err.println("GameServer failed to start: " + e.getMessage());
                e.printStackTrace();
            }
        }

    public void handleClientMessage(PlayerAction action) {
        //System.out.println("In GameServer: handleClientMessage");
        if (!players.containsKey(action.getPlayerId()))
        {
            //System.out.println("In GameServer: handleClientMessage");
        }
        actionQueue.offer(action);
    }

    public void receiveUpdate(PlayerAction update) {
        actionQueue.offer(update);
    }

    private void startGameLoop() {
//        System.out.println("In GameServer: startGameLoop");
        gameLoopExecutor.scheduleAtFixedRate(this::updateGame, 0, 1000 / 60, TimeUnit.MILLISECONDS);
        blocks = blockService.findAllBlocks();
    }

    private void updateGame() {
        playersActedThisFrame.clear();
        quadTree.clear();  //TODO should we be clearing the tree every frame? Seems odd
        for (Player player : players.values()) {
            //System.out.println("In GameServer: updateGame: addPlayers to quadTree");
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
        //System.out.println("In GameServer: updateGame: post quadTree.insert");
        processActions();
        updateProjectiles();
//        updatePlayerPositions();
        checkItemCollisions();
//        checkProjectileCollisions();
        //SAVE STATE TO DB TODO !!
        sendGameStateToClients();
    }

    private void processActions() {
        //System.out.println("In GameServer: processActions");
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
                case IDLE:
                    player.setCurrentAction(Player.Action.IDLE);
                    break;
                case MOVE:
                    movePlayer(player, action.getDirection());
                    player.setCurrentAction(Player.Action.MOVE);
                    break;
                case ATTACK:
                    createProjectile(player, action.getDirection());
                    player.setCurrentAction(Player.Action.ATTACK);
                    System.out.println("Attacking");
                    break;
                case PLACE:
                    placeBlock(player, action);
                    player.setCurrentAction(Player.Action.PLACE);
                    break;
                case BREAK:
                    breakBlock(player, action);
                    // player.setCurrentAction(Player.Action.BREAK);
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

    private void movePlayer(Player player, Direction direction) {
        //System.out.println("In GameServer: movePlayer");

        switch (direction) {
            case UP:
                player.setY(player.getY() - 1);
                player.setDirection(Direction.UP);
                break;
            case DOWN:
                player.setY(player.getY() + 1);
                player.setDirection(Direction.DOWN);
                break;
            case LEFT:
                player.setX(player.getX() - 1);
                player.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                player.setX(player.getX() + 1);
                player.setDirection(Direction.RIGHT);
                break;
        }
        System.out.println(player.getX() + " " + player.getY());
    }

    private void createProjectile(Player player, Direction direction) {
        Projectile projectile = new Projectile(player.getX(), player.getY(), direction, player.getId());
        System.out.println("Projectile created");
        projectiles.add(projectile);
    }

    private void placeBlock(Player player, PlayerAction action) {
        int x = action.getX();
        int y = action.getY();
        int width = 32;
        int height = 32;
        //String sprite = action.getSprite();

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

        Block tempBlock = new Block(x, y, width, height, true);

        List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), tempBlock);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Block && ((Block) entity).isCollide()) {
                if (intersects(tempBlock, entity)) {
//                    sendResponseToPlayer(player, "No Space"); TODO
                    System.out.println("NO SPACE!!!!!");
                    return;
                }
            }
        }
        System.out.println("Adding a block");
        blocks.add(tempBlock);
        blockService.addBlock(tempBlock);

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
        //System.out.println("In GameServer: checkItemCollisions");

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
//    private void checkProjectileCollisions() {
//        //System.out.println("In GameServer: checkItemCollisions");
//
//        Iterator<Projectile> iterator = projectiles.iterator();
//        while (iterator.hasNext()) {
//            Projectile projectile = iterator.next();
//            List<Entity> nearbyEntities = quadTree.retrieve(new ArrayList<>(), projectile);
//            for (Entity entity : nearbyEntities) {
//                if (entity instanceof Player) {
//                    Player player = (Player) entity;
//                    if (intersects(projectile, player) && !projectile.getPlayerId().equals(player.getId())) {
////                        Item item = new Item(0, itemEntity.getName(), itemEntity.getSprite());
////                        inventoryService.addItemToPlayer(player, item);
//                        System.out.println("ITS A HIT!!!!   ProjectilePlayerID: " + projectile.getPlayerId() + " Player ID: " + player.getId());
//                        player.setHp(player.getHp() - 1);
//                        if (player.getHp() <= 0){
//                            System.out.println("player hp is: " + player.getHp() + " time to figure out death mechanic");
//                        }
//                        iterator.remove();
//                        break;
//                    }
//                }
//            }
//        }
//    }
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
        //System.out.println("In GameServer: updatePlayerPositions");
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
                        iterator.remove();
                        break;
                    }
                } else if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (intersects(projectile, player) && !projectile.getPlayerId().equals(player.getId())) {
                        player.setHp(player.getHp() - projectile.getDamage());
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
        //System.out.println("In GameServer: sendGameStateToClients");
        //GameState gameState = new GameState(players.values(), projectiles, blocks, itemEntities);
        List<Projectile> visibleProjectiles = null;
        List<Player> visiblePlayers = null;
        System.out.println("There are: " + players.size() + " players in the game");
        for (Player player : players.values()) {
            System.out.println("Sending message to this player: " + player.getId());
            visiblePlayers = getVisiblePlayers(player);
            System.out.println("There are: " + visiblePlayers.size() + " players visible to this player");
            visiblePlayers.add(player);
            System.out.println("Added the player");
            visibleProjectiles = getVisibleProjectiles(player);
            System.out.println("There are: " + visibleProjectiles.size() + " projectiles visible to this player");
            GameState gameState = new GameState(player, visiblePlayers, visibleProjectiles, blocks, itemEntities);
            String message = serializeGameState(gameState);
            System.out.println("There are this many sessions: " + sessions.size());
            WebSocketSession playerSession = sessions.get(player.getId());
            System.out.println("Sending message to this player: " + player.getId() + " with message: " + message);
            if (playerSession != null && playerSession.isOpen()) {
                try {
                    playerSession.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    System.err.println("Error when attempting to send game state to client: " + e.getMessage());
                }
                System.out.println("Sending message to player: " + player.getId() + " with message: " + message);
            }

        }

//        GameState gameState = new GameState(visiblePlayers, visibleProjectiles, blocks, itemEntities);
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
        //System.out.println("In GameServer: getVisibleProjectiles");

        List<Projectile> visibleProjectiles = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            System.out.println("Checking projectile: " + projectile.getX() + " " + projectile.getY());
            if (isWithinVisibilityRange(player, projectile)) {
                visibleProjectiles.add(projectile);
            }
        }
        return visibleProjectiles;
    }

    private boolean isWithinVisibilityRange(Entity entity1, Entity entity2) {
        //System.out.println("In GameServer: isWithinVisibilityRange");

        int dx = entity1.getX() - entity2.getX();
        int dy = entity1.getY() - entity2.getY();
        System.out.println("Distance between entities: " + Math.sqrt(dx * dx + dy * dy));
        return dx * dx + dy * dy <= VISIBILITY_RANGE * VISIBILITY_RANGE;
    }

    private boolean isWithinBreakRange(Entity entity1, Entity entity2) {
        int dx = entity1.getX() - entity2.getX();
        int dy = entity1.getY() - entity2.getY();
        return dx * dx + dy * dy <= BREAK_RANGE * BREAK_RANGE;
    }

    private String serializeGameState(GameState gameState) {
        System.out.println("In GameServer: serializeGameState");
        try {
            GsonBuilder gsonBuilder = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    // Skip fields that might cause circular references
                    return f.getDeclaringClass() == User.class || 
                           f.getName().equals("user");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .serializeNulls()
            .registerTypeAdapter(List.class, new TypeAdapter<List>() {
                @Override
                public void write(JsonWriter out, List value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.beginArray();
                    for (Object item : value) {
                        if (item != null) {
                            out.value(gson.toJson(item));
                        } else {
                            out.nullValue();
                        }
                    }
                    out.endArray();
                }

                @Override
                public List read(JsonReader in) throws IOException {
                    return null; // We don't need deserialization for this case
                }
            });

        Gson customGson = gsonBuilder.create();
        String temp = customGson.toJson(gameState);
        if (temp != null) {
            System.out.println("Serialized state length: " + temp.length());
            return temp;
        } else {
            System.err.println("Serialization produced null result");
            return "{}"; // Return empty JSON object as fallback
        }
    } catch (Exception e) {
        e.printStackTrace(); // This will print the full stack trace
        System.err.println("Error when serializing game state: " + e.getMessage());
        return "{}"; // Return empty JSON object as fallback
    }
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

    public void removeSession( Long playerId){
        sessions.remove(playerId);
    }

    public void removePlayer(Long playerId){
        players.remove(playerId);
    }

    public void addSession(Long playerId, WebSocketSession session){
        sessions.put(playerId, session);
    }

    public void addPlayer(Player player) {
            players.put(player.getId(), player);
    }

    public Player getPlayer(Long playerId) {
        // Return the player instance from your players collection
        return players.get(playerId);
    }

}