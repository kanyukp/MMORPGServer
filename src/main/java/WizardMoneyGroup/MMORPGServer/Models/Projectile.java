package WizardMoneyGroup.MMORPGServer.Models;

import javax.persistence.*;
import java.io.Serializable;
@Entity
public class Projectile implements Serializable {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(nullable = false,updatable = false)
    // private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,updatable = false)
    private long entityID;

    //    private String username;
    @Column
    private int Xpos;
    @Column
    private int Ypos;
    @Column
    private  int Xdir;

    @Column
    private int Ydir;

    @Column
    private int Dmg;

    public Projectile(){}

    public Projectile(long entityID, int Xpos, int Ypos, int Xdir, int Ydir, int Dmg)
    {
        this.entityID = entityID;
        this.Xpos = Xpos;
        this.Ypos = Ypos;
        this.Xdir = Xdir;
        this.Ydir = Ydir;
        this.Dmg = Dmg;
    }

    @Override
    public String toString () {
        return "Projectile{" + "entityID=" + entityID + ", Xpos: " + Xpos + ", Ypos: " + Ypos + "}";
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }

    public int getXpos() {
        return Xpos;
    }

    public void setXpos(int xpos) {
        Xpos = xpos;
    }

    public int getYpos() {
        return Ypos;
    }

    public void setYpos(int ypos) {
        Ypos = ypos;
    }

    public int getYdir() {
        return Ydir;
    }

    public void setYdir(int ydir) {
        Ydir = ydir;
    }

    public int getXdir() {
        return Xdir;
    }

    public void setXdir(int xdir) {
        Xdir = xdir;
    }

    public int getDmg() {
        return Dmg;
    }

    public void setDmg(int dmg) {
        Dmg = dmg;
    }
}
