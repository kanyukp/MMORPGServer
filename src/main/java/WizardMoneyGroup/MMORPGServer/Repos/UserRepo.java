package WizardMoneyGroup.MMORPGServer.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import testgrouppleaseignore.demo.Models.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("select s from User s where s.username = ?1")
    Optional<User> findUserByUsername(String username);

    @Query("select s from User s where s.username = ?1 and s.password = ?2")
    Optional<User> findUserByUsernameAndPassword(String username, String password);

}
