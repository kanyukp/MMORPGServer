package WizardMoneyGroup.MMORPGServer.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
public class Player extends WorldEntity {
    private int x,y,width,height;
    private int hp;
    private Player.Action currentAction;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private List<Item> inventory;

    private boolean inventoryOpen;
    private String sprite;

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Direction direction;

    public enum Action {
        IDLE, MOVE, ATTACK, PLACE, BREAK, DROP, OPEN_INVENTORY, CLOSE_INVENTORY

    }

    public Player() {
        // Leave empty or initialize with default values if needed
    }

    public Player(int x, int y, int width, int height, int hp, String sprite ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.currentAction = Player.Action.IDLE;
        this.inventory = new ArrayList<>();
        this.inventoryOpen = false;
        this.sprite = sprite;
        this.direction = Direction.LEFT;
    }

    // Add getter and setter for direction
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Player.Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Player.Action currentAction) {
        this.currentAction = currentAction;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item){
        inventory.add(item);
    }

    public void removeItem(Item item){
        inventory.remove(item);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}