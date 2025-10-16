package rs.ac.fon.gymtracker.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.fon.gymtracker.infrastructure.security.JwtAuthFilter;
import rs.ac.fon.gymtracker.infrastructure.security.JwtUtil;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainers/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())     // ili .disable()
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
