package WizardMoneyGroup.MMORPGServer.DAO;

import WizardMoneyGroup.MMORPGServer.Models.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerInventoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public PlayerInventoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Item> getPlayerInventory(Long playerId) {
        String sql = "SELECT i.ItemID, i.Name, i.Sprite FROM Items i " +
                     "JOIN PlayerInventory pi ON i.ItemID = pi.ItemID " +
                     "WHERE pi.PlayerID = ? ";
        return jdbcTemplate.query(sql, new ItemDAO.ItemRowMapper(), playerId);
    }

    public void addItemToPlayer(Long playerId, int itemId) {
        String sql = "INSERT INTO PlayerInventory (PlayerID, ItemID) VALUES (?,?)";
        jdbcTemplate.update(sql, playerId, itemId);
    }

    public void removeItemFromPlayer(Long playerId, int itemId) {
        String sql = "DELETE FROM PlayerInventory WHERE PlayerID = ? AND ItemID = ?";
        jdbcTemplate.update(sql, playerId, itemId);
    }

}
