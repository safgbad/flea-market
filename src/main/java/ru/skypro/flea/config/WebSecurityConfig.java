package ru.skypro.flea.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.flea.model.enums.Role;
import ru.skypro.flea.repository.UserRepository;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    @Bean
    @Transactional
    public JdbcUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder,
                                                     DataSource dataSource,
                                                     UserRepository userRepository) {
        String email = "admin@admin.com";
        UserDetails user =
                User.builder()
                        .username(email)
                        .password("admin")
                        .passwordEncoder(passwordEncoder::encode)
                        .authorities(Role.ADMIN.name())
                        .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (users.userExists(email)) {
            log.info("Admin account has already been created");
            return users;
        }
        users.createUser(user);
        var adminOptional = userRepository.findByEmail(email);
        if (adminOptional.isEmpty()) {
            log.error("ADMIN ACCOUNT HAS NOT FOUND");
            throw new RuntimeException();
        }
        var admin = adminOptional.get();
        admin.setFirstName("Admin");
        admin.setPhone("+7(000)000-00-00");
        userRepository.save(admin);

        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
//                .authorizeHttpRequests(
//                        authorization ->
//                                authorization
//                                        .mvcMatchers(AUTH_WHITELIST)
//                                        .permitAll()
//                                        .mvcMatchers("/ads/**", "/users/**")
//                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
