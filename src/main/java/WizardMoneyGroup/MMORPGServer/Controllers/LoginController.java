package WizardMoneyGroup.MMORPGServer.Controllers;

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
    public ResponseEntity<String> register(@RequestBody AuthRequest request){
        boolean success = loginService.registerUser(request.username, request.password);
        if (success) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = loginService.loginUser(request.username, request.password);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}
