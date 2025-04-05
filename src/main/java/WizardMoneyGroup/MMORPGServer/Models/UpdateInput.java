package WizardMoneyGroup.MMORPGServer.Models;

//import javax.persistence.*;

public class UpdateInput {

    private Long id;

    private String username;

    private int action;
    private int direction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public UpdateInput(String username, int action, int direction) {
        this.username = username;
        this.action = action;
        this.direction = direction;
    }

//    public Update(String username, int action) {
//        this.username = username;
//        this.action = action;
//    }

    public UpdateInput(String username, int direction) {
        this.username = username;
        this.direction = direction;
    }

    public UpdateInput(String username) {
        this.username = username;
        this.direction = 0;
    }

    public UpdateInput() {
        this.direction = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
