package WizardMoneyGroup.MMORPGServer.Models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    private String name;
    private String sprite;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "player_id", nullable = true)
    private Player owner;

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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
