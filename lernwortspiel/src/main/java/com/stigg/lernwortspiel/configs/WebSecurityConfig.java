package com.stigg.lernwortspiel.configs;


import com.stigg.lernwortspiel.enteties.Role;
import com.stigg.lernwortspiel.enteties.User;
import com.stigg.lernwortspiel.repositories.EmailConfirmationTokenRepository;
import com.stigg.lernwortspiel.repositories.RoleRepository;
import com.stigg.lernwortspiel.repositories.UserRepository;
import com.stigg.lernwortspiel.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()  // access to static resources
                        .requestMatchers("/app/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((session) -> session
                        .loginPage("/app/login")
                        .defaultSuccessUrl("/app/home",true)
                        .failureUrl("/app/login?error=true")
                        .permitAll()
                )
                .rememberMe((rememberMe) -> rememberMe
                        .rememberMeServices(rememberMeServices())
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .permitAll());

        return http.build();
    }

    //  My UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(mailSender,userRepository,roleRepository,emailConfirmationTokenRepository, passwordEncoder());
    }


    //  Auth provider for db user details storage
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //    Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Session registry for session management
    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }


    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices =
                new SpringSessionRememberMeServices();
        // optionally customize
        rememberMeServices.setRememberMeParameterName("remember-me");
        rememberMeServices.setAlwaysRemember(false);
        return rememberMeServices;
    }

//        UserInit
//    @Bean
//    public ApplicationRunner initializer(UserRepository userRepository) {
//        return args -> {
//
//            Role role_admin = new Role("ROLE_ADMIN");
//            Role role_user = new Role("ROLE_USER");
//
//            User user_admin = new User(passwordEncoder().encode("280211"), "katyshev.yevhen@gmail.com", "Yevhen", "Katyshev", "+380680595514",
//                                        true, true, true,true, LocalDate.of(1997,2,28), new HashSet<>(Set.of(role_admin,role_user)));
//
//            roleRepository.save(role_user);
//            roleRepository.save(role_admin);
//            userRepository.save(user_admin);
//        };
//    }

}