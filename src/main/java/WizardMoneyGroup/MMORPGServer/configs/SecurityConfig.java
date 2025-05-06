package WizardMoneyGroup.MMORPGServer.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // 🔵 Enable CORS
                .and()
                .csrf().disable() // 🔵 Disable CSRF for API
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // 🔵 Allow public access to /api/auth/**
                        .requestMatchers("/game/**").permitAll() // ✅ Allow WebSocket endpoint
                        .anyRequest().authenticated()
                )
                .httpBasic(); // (Optional) Allow basic auth if you need
        return http.build();
    }
}