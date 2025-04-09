package WizardMoneyGroup.MMORPGServer.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerInventoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public PlayerInventoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
