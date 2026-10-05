package br.cefetmg.comunidadettf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.cefetmg.comunidadettf.model.game.GameUser;
import br.cefetmg.comunidadettf.repository.GameUserRepository;

@Configuration
public class AdminUserInitializer {

    @Bean
    CommandLineRunner createAdminUser(
            GameUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.login:admin}") String login,
            @Value("${app.admin.password:admin123}") String password,
            @Value("${app.admin.nome:Administrador}") String nome) {
        return args -> {
            GameUser admin = userRepository.findByLoginIgnoreCase(login).orElseGet(GameUser::new);
            admin.setLogin(login);
            admin.setNome(nome);
            admin.setRole("ADMIN");
            if (admin.getPasswordHash() == null || admin.getPasswordHash().isBlank()) {
                admin.setPasswordHash(passwordEncoder.encode(password));
            }
            userRepository.save(admin);
        };
    }
}