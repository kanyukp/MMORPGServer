package WizardMoneyGroup.MMORPGServer.Services;

import WizardMoneyGroup.MMORPGServer.DAO.PlayerRepository;
import WizardMoneyGroup.MMORPGServer.DAO.UserRepository;
import WizardMoneyGroup.MMORPGServer.GameServer;
import WizardMoneyGroup.MMORPGServer.Models.Player;
import WizardMoneyGroup.MMORPGServer.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final GameServer gameServer;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;

    @Autowired
    public LoginService(UserRepository userRepository,
                        PlayerRepository playerRepository, GameServer gameServer) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.gameServer = gameServer;
    }

//    public boolean registerUser(String username, String password){
//        if (userRepository.findByUsername(username).isPresent()){
//            return false; //TODO
//        }
//        String passwordHash = passwordEncoder.encode(password);
//        User user = new User();
//        user.setUsername(username);
//        user.setPasswordHash(passwordHash);
//        userRepository.save(user);
//        Long userId = userRepository.findByUsername(username).get().getId();
//
//        playerRepository.save(player);
//        return true;
//    }

    @Transactional
    public Optional<Player> registerUser(String username, String password) {
        // Check if a user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return Optional.empty();
        }

        // Create and save a new user
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        // Create and save a new player
        Player player = new Player(0,0,32,32,10,"playertest.png");
        player.setId(savedUser.getId());
        player.setUser(savedUser);


        // ... set other default properties as needed

        playerRepository.save(player);

        return Optional.of(player);
    }


    public Optional<Player> loginUser(String username, String password){
        Optional<User> userOpt = userRepository.findByUsername(username);
        Player tempPlayer = null;
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Looking for user!");
            if(passwordEncoder.matches(password, user.getPasswordHash())){
                System.out.println("Found user!");
                tempPlayer = playerRepository.findByUserId(user.getId()).get();
                this.gameServer.addPlayer(tempPlayer);
                return Optional.of(tempPlayer);
            }
        }
        return Optional.empty();
    }

}
