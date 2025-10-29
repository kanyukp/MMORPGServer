package WizardMoneyGroup.MMORPGServer.Services;

import WizardMoneyGroup.MMORPGServer.DAO.ItemDAO;
import WizardMoneyGroup.MMORPGServer.DAO.PlayerInventoryDAO;
import WizardMoneyGroup.MMORPGServer.Models.Item;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final PlayerInventoryDAO playerInventoryDAO;
    private final ItemDAO itemDAO;

    @Autowired
    public InventoryService(PlayerInventoryDAO playerInventoryDAO, ItemDAO itemDAO){
        this.playerInventoryDAO = playerInventoryDAO;
        this.itemDAO = itemDAO;
    }

    public void loadPlayerInventory(Player player){
        List<Item> inventory = playerInventoryDAO.getPlayerInventory(player.getId());
        player.getInventory().addAll(inventory);
    }

    public void addItemToPlayer(Player player, Item item) {
        player.addItem(item);
        playerInventoryDAO.addItemToPlayer(player.getId(), item.getItemId());
    }

    public void removeItemFromPlayer(Player player, Item item){
        player.removeItem(item);
        playerInventoryDAO.removeItemFromPlayer(player.getId(), item.getItemId());
    }

}