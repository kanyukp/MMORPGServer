package WizardMoneyGroup.MMORPGServer.Models;

import javax.persistence.*;
import java.io.Serializable;

import static testgrouppleaseignore.demo.Constants.*;

@Entity
public class Player implements Serializable {

   // @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(nullable = false,updatable = false)
   // private Long id;

    @Id
    @Column(nullable = false,updatable = false,length = 16)
    private String username;

    private int Xpos;


    private int Ypos;

    private int hp;

    private int direction;

    public Player(){}



    public Player(String username, int x, int y)
    {
        this.username = username;
        //this.password = password;
        this.direction = DOWN;
        this.Xpos = x;
        this.Ypos = y;
    }
    public Player (Player player){
        this.username = player.username;
        //this.password = password;
        this.direction = player.direction;
        this.Xpos = player.Xpos;
        this.Ypos = player.Ypos;
    }

    public int getYpos() {
        return Ypos;
    }
    public void setYpos(int ypos) {
        Ypos = ypos;
    }

    public int getXpos() {
        return Xpos;
    }
    public void setXpos(int xpos) {
        Xpos = xpos;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString () {
        return "Player{" + "username=" + username + ", Xpos: " + Xpos + ", Ypos: " + Ypos + "}";
    }


}
