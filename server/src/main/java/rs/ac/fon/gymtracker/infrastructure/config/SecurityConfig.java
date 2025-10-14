package rs.ac.fon.gymtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // za Postman i jednostavne POST/PUT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainers/login").permitAll()
                        .requestMatchers("/api/**").permitAll()   // <<< pusti sve API-je u dev-u
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())  // može i .disable() ako ne želiš basic
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
