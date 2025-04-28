package WizardMoneyGroup.MMORPGServer.Services;

import WizardMoneyGroup.MMORPGServer.DAO.UserRepository;
import WizardMoneyGroup.MMORPGServer.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean registerUser(String username, String password){
        if (userRepository.findByUsername(username).isPresent()){
            return false; //TODO
        }
        String passwordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
        return true; //TODO add link to character(player) maybe?
    }

    public Optional<User> loginUser(String username, String password){
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Looking for user!");
            if(passwordEncoder.matches(password, user.getPasswordHash())){
                System.out.println("Found user!");
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
