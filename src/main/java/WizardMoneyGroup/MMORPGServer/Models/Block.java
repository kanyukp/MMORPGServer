package WizardMoneyGroup.MMORPGServer.Models;

public class Block implements Entity {
    private int x,y,width,height;
    private boolean collide;
    private String sprite;

    public Block(int x, int y, int width, int height, boolean collide, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collide = collide;
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
