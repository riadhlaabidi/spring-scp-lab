package config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class RestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("SUPERADMIN")
                .requestMatchers(HttpMethod.POST, "/accounts/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/accounts/**").hasAnyRole("SUPERADMIN", "ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("SUPERADMIN", "ADMIN", "USER")
                // Deny any request that doesn't match any authorization rule
                .anyRequest().denyAll())
                .httpBasic(withDefaults())
                .csrf(CsrfConfigurer::disable);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "USER")
                .build();
        UserDetails superAdmin = User
                .withUsername("superadmin")
                .password(passwordEncoder.encode("superadmin"))
                .roles("SUPERADMIN", "ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin, superAdmin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
