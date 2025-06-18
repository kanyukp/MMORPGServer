package WizardMoneyGroup.MMORPGServer.Models;

public class Projectile implements Entity {
    private int x,y,width,height;
    private int originX, originY;
    private int maxTravelDistance;
    private int damage;
    private PlayerAction.Direction direction;
    private String sprite;



    public Projectile(int x, int y, int width, int height, int originX, int originY, int maxTravelDistance, int damage, PlayerAction.Direction direction, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.maxTravelDistance = maxTravelDistance;
        this.damage = damage;
        this.direction = direction;
        this.sprite = "fireball.png"; //TODO
    }

    public Projectile(int x, int y, PlayerAction.Direction direction) {
        this.x = x;
        this.y = y;
        // this.sprite = sprite;
        this.width = 16;
        this.height = 16;
        this.originX = x;
        this.originY = y;
        this.maxTravelDistance = 320;
        this.damage = 1;
        this.direction = direction;
        this.sprite = "fireball.png";
    }

    public void move(){
        switch (direction) {
            case UP:
                y -= 1;
                break;
            case DOWN:
                y += 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }
    }
    public boolean hasExceededMaxDistance() {
        int dx = x-originX;
        int dy = y-originY;
        return dx * dx + dy * dy > maxTravelDistance * maxTravelDistance;
    }

    @Override
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

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int getMaxTravelDistance() {
        return maxTravelDistance;
    }

    public void setMaxTravelDistance(int maxTravelDistance) {
        this.maxTravelDistance = maxTravelDistance;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public PlayerAction.Direction getDirection() {
        return direction;
    }

    public void setDirection(PlayerAction.Direction direction) {
        this.direction = direction;
    }
}