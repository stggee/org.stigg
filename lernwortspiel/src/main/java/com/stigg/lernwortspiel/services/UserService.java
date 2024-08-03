package com.stigg.lernwortspiel.services;

import com.stigg.lernwortspiel.dto.UserRegisterDTO;
import com.stigg.lernwortspiel.enteties.EmailConfirmationToken;
import com.stigg.lernwortspiel.enteties.User;
import com.stigg.lernwortspiel.repositories.EmailConfirmationTokenRepository;
import com.stigg.lernwortspiel.repositories.RoleRepository;
import com.stigg.lernwortspiel.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegisterDTO userDTO){
        User newUser = User.fromDTO(userDTO, passwordEncoder, new HashSet<>(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow())));
        userRepository.save(newUser);
        sendConfirmationEmail(newUser);
    }

    public void sendConfirmationEmail(User user) {
        EmailConfirmationToken token = new EmailConfirmationToken(user);
        emailConfirmationTokenRepository.save(token);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("lernwortspiel@ukr.net");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://192.168.0.189:8080/app/confirm-account?token=" + token.getToken());

        mailSender.send(mailMessage);
    }

    public void confirmUser(String token) {
        EmailConfirmationToken confirmationToken = emailConfirmationTokenRepository.findByToken(token).orElseThrow();

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        emailConfirmationTokenRepository.delete(confirmationToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
