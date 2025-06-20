package WizardMoneyGroup.MMORPGServer.Models;

public class PlayerAction {

    public enum ActionType {
        IDLE, MOVE, ATTACK, PLACE, BREAK, DROP, OPEN_INVENTORY, CLOSE_INVENTORY
    }

    private Long playerId;
    private ActionType actionType;
    private Direction direction;
    private int x, y, width, height;
    private String sprite;
    private String itemName;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "playerId='" + playerId + '\'' +
                ", actionType=" + actionType +
                ", direction=" + direction +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", sprite='" + sprite + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
