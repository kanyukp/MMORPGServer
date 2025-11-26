package WizardMoneyGroup.MMORPGServer.DAO;

import WizardMoneyGroup.MMORPGServer.Models.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

}