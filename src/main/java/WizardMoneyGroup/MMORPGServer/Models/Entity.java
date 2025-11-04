package WizardMoneyGroup.MMORPGServer.Models;


import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class Entity {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    public Entity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }
}
