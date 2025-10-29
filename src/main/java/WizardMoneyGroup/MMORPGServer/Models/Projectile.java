package WizardMoneyGroup.MMORPGServer.Models;

import java.util.concurrent.atomic.AtomicLong;

public class Projectile implements Entity {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(20);
    private final long id;
    private int x,y,width,height;
    private int originX, originY;
    private int maxTravelDistance;
    private int damage;
    private Direction direction;
    private String sprite;
    private Long playerId;



    public Projectile(int x, int y, int width, int height, int originX, int originY, int maxTravelDistance, int damage, Direction direction, String sprite) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.maxTravelDistance = maxTravelDistance;
        this.damage = damage;
        this.direction = direction;
        this.sprite = "Firebolt.png"; //TODO
    }

    public Projectile(int x, int y, Direction direction, Long playerId) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.playerId = playerId;
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
        this.sprite = "Firebolt.png";
    }

    // Add getter for id
    public long getId() {
        return id;
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
                System.out.println("Left");
                x -= 1;
                break;
            case RIGHT:
                System.out.println("Right");
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

}