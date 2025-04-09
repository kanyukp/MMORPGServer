package WizardMoneyGroup.MMORPGServer.DAO;

import WizardMoneyGroup.MMORPGServer.Models.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ItemDAO {

    private final JdbcTemplate jdbcTemplate;

    public ItemDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Item> getAllItems() {
        String sql = "SELECT * FROM Items";
        return jdbcTemplate.query(sql, new ItemRowMapper());
    }

    public void saveItem(Item item) {
        String sql = "INSERT INTO Items (Name, Sprite) VALUES (?,?";
        jdbcTemplate.update(sql, item.getName(), item.getSprite());
    }

    private static class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Item(rs.getInt("ItemID"), rs.getString("Name"), rs.getString("Sprite"));
        }
    }
}
