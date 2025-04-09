package WizardMoneyGroup.MMORPGServer.Models;

public class Item {
    private int itemId;
    private String name;
    private String sprite;

    public Item(int itemId, String name, String sprite) {
        this.itemId = itemId;
        this.name = name;
        this.sprite = sprite;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
