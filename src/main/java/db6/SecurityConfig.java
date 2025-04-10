package db6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import db6.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
            // Public endpoints
            .requestMatchers("/login", "/signup", "/error", "/css/**", "/js/**", "/images/**").permitAll()
            // Protected endpoints
            .anyRequest().authenticated()
        )

        // Enable CSRF token in cookies
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

        // Setup logining
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/fam-tree", true)
            .failureUrl("/login?error")
            .permitAll()
        )

        // Setup logouting
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}