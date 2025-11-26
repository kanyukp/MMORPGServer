package WizardMoneyGroup.MMORPGServer.DAO;

import WizardMoneyGroup.MMORPGServer.Models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {}