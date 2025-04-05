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

    private int xpos;


    private int ypos;

    private int hp;

    private int direction;

    public Player(){}



    public Player(String username, int x, int y)
    {
        this.username = username;
        //this.password = password;
        this.direction = DOWN;
        this.xpos = x;
        this.ypos = y;
    }
    public Player (Player player){
        this.username = player.username;
        //this.password = password;
        this.direction = player.direction;
        this.xpos = player.xpos;
        this.ypos = player.ypos;
    }

    public int getYpos() {
        return ypos;
    }
    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public int getXpos() {
        return xpos;
    }
    public void setXpos(int xpos) {
        this.xpos = xpos;
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
        return "Player{" + "username=" + username + ", Xpos: " + xpos + ", Ypos: " + ypos + "}";
    }


}
