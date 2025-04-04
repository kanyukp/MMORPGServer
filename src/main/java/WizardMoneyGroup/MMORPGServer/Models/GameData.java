package WizardMoneyGroup.MMORPGServer.Models;

import java.util.List;

public class GameData {
    public Player currPlayer;
    public Player[] players ;
    public Projectile[] projectiles;

    public GameData(Player currPlayer, Player[] players, Projectile[] projectiles) {
        this.currPlayer = currPlayer;
        this.players = players;
        this.projectiles = projectiles;
    }

    public GameData(Player currPlayer, Player[] players) {
        this.currPlayer = currPlayer;
        this.players = players;
    }
    public GameData(Player currPlayer, List<Player> players) {
        this.currPlayer = currPlayer;
        Player[] temp = new Player[players.size()];
        for (int i = 0; i < players.size(); i++){
            temp[i] = players.get(i);
        }
        this.players = temp;
    }
    public GameData(Player currPlayer, List<Player> players, List<Projectile> projectiles) {
        this.currPlayer = currPlayer;
        Player[] temp = new Player[players.size()];
        for (int i = 0; i < players.size(); i++){
            temp[i] = players.get(i);
        }
        this.players = temp;

        Projectile[] tempProj = new Projectile[projectiles.size()];
        for (int j = 0; j < projectiles.size(); j++){
            tempProj[j] = projectiles.get(j);
        }
        this.projectiles = tempProj;
    }

    public GameData(Player currPlayer, Projectile[] projectiles) {
        this.currPlayer = currPlayer;
        this.projectiles = projectiles;
    }

    public GameData(Player[] players, Projectile[] projectiles) {
        this.players = players;
        this.projectiles = projectiles;
    }

    public GameData(Player currPlayer) {
        this.currPlayer = currPlayer;
    }
    public GameData(Player[] players) {
        this.players = players;
    }
    public GameData(Projectile[] projectiles) {
        this.projectiles = projectiles;
    }

    public GameData() {

    }
}
