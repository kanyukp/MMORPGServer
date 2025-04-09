package WizardMoneyGroup.MMORPGServer.Models;

import java.util.Collection;
import java.util.Map;

public class GameState {

    private final Map<String, Player> players;
    private final Collection<Projectile> projectiles;

    public GameState(Map<String, Player> players, Collection<Projectile> projectiles) {
        this.players = players;
        this.projectiles = projectiles;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Collection<Projectile> getProjectiles() {
        return projectiles;
    }
}
