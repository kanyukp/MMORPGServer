package WizardMoneyGroup.MMORPGServer.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testgrouppleaseignore.demo.Models.Player;
import testgrouppleaseignore.demo.Models.Projectile;
import testgrouppleaseignore.demo.Models.UpdateInput;
import testgrouppleaseignore.demo.Services.GameDataService;
import testgrouppleaseignore.demo.Services.PlayerService;
import testgrouppleaseignore.demo.Services.ProjectileService;
import testgrouppleaseignore.demo.Services.UpdateService;

import java.util.List;

@RestController
@RequestMapping("/gameData")
public class GameDataResource {

    private final GameDataService gameDataService;
    private final PlayerService playerService;
    private final UpdateService updateService;
    private final ProjectileService projectileService;

    public GameDataResource(GameDataService gameDataService, PlayerService playerService, UpdateService updateService, ProjectileService projectileService)
    {
        this.gameDataService = gameDataService;
        this.playerService = playerService;
        this.updateService = updateService;
        this.projectileService = projectileService;
    }

    @PostMapping("/all")
    public ResponseEntity<GameData> getAllData(@RequestBody UpdateInput updateInput) {
        // System.out.println("In Game Data ***************************************");
        System.out.println(updateInput.getUsername());
        this.updateService.addUpdate(updateInput);
        Player player = playerService.findPlayer(updateInput.getUsername());
        List<Player> players = playerService.findAllPlayers();
        List<Projectile> projectiles = projectileService.findAllProjectiles();
        GameData response = new GameData(player, players, projectiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
