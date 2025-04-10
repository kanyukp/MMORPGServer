package WizardMoneyGroup.MMORPGServer.Models;

import java.util.ArrayList;
import java.util.List;

public class Player implements Entity {
    private int x,y,width,height;
    private int hp;
    private Player.Action currentAction;
    private List<Item> inventory;
    private boolean inventoryOpen;

    private String id;

    public enum Action {
        IDLE, MOVE, ATTACK, PLACE, BREAK, DROP, OPEN_INVENTORY, CLOSE_INVENTORY

    }

    public Player(int x, int y, int width, int height, int hp ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.currentAction = Player.Action.IDLE;
        this.inventory = new ArrayList<>();
        this.inventoryOpen = false;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}