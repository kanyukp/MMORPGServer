package WizardMoneyGroup.MMORPGServer.Controllers;

import WizardMoneyGroup.MMORPGServer.Models.Player;
import WizardMoneyGroup.MMORPGServer.Models.User;
import WizardMoneyGroup.MMORPGServer.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 📦 Small helper class to receive JSON body
    public static class AuthRequest {
        public String username;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request){
        System.out.println("Registering user: ******************************************************" + request.username);
        Optional<Player> playerOpt = loginService.registerUser(request.username, request.password);
        if (playerOpt.isPresent()) {
            return ResponseEntity.ok(playerOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Unable to create player account or username already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println("Logging in user: ******************************************************" + request.username);
        Optional<Player> playerOpt = loginService.loginUser(request.username, request.password);
        if (playerOpt.isPresent()) {
            return ResponseEntity.ok(playerOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}
