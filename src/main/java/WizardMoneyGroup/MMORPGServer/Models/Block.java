package WizardMoneyGroup.MMORPGServer.Models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;

import java.util.concurrent.atomic.AtomicLong;

@jakarta.persistence.Entity
public class Block implements Entity {
//    private static final AtomicLong ID_GENERATOR = new AtomicLong(20);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int x,y,width,height;
//    private final long id;
    private boolean collide;
    private String sprite;

    public Block(){}

    public Block(int x, int y, int width, int height, boolean collide) {
//        this.id = ID_GENERATOR.incrementAndGet();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collide = collide;
        this.sprite = "TestMap.png";
    }

    public Block(int x, int y) {
//        this.id = ID_GENERATOR.incrementAndGet();
        this.x = x;
        this.y = y;
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

    public boolean isCollide() {
        return collide;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
