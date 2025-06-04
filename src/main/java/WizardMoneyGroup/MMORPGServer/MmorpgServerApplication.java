package WizardMoneyGroup.MMORPGServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("WizardMoneyGroup.MMORPGServer")
public class MmorpgServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmorpgServerApplication.class, args);
	}

}