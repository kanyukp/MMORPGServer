package WizardMoneyGroup.MMORPGServer.Models;

import java.util.Collection;
import java.util.Map;

public class GameState {

    private final Collection<Player> players;
    private final Collection<Projectile> projectiles;
    private final Collection<Block> blocks;
    private final Collection<ItemEntity> itemEntities;

    public GameState(Collection<Player> players, Collection<Projectile> projectiles, Collection<Block> blocks, Collection<ItemEntity> itemEntities) {
        this.players = players;
        this.projectiles = projectiles;
        this.blocks = blocks;
        this.itemEntities = itemEntities;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Collection<Projectile> getProjectiles() {
        return projectiles;
    }
}
