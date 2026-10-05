package br.cefetmg.comunidadettf.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import br.cefetmg.comunidadettf.dto.auth.AuthResponse;
import br.cefetmg.comunidadettf.dto.auth.LoginRequest;
import br.cefetmg.comunidadettf.dto.auth.RegisterRequest;
import br.cefetmg.comunidadettf.model.game.GameUser;
import br.cefetmg.comunidadettf.repository.GameUserRepository;
import br.cefetmg.comunidadettf.security.JwtService;

@Service
@Transactional
public class AuthService {

    private final GameUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(GameUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

    System.out.println("ENTROU NO REGISTER");

    String login = normalize(request.login());
    String password = normalize(request.password());
    String nome = normalize(request.nome());

    validateRegister(login, password, nome);

    System.out.println("LOGIN: " + login);

    if (userRepository.existsByLoginIgnoreCase(login)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja existe.");
    }

    GameUser user = new GameUser();

    user.setLogin(login);
    user.setNome(nome);
    user.setRole("USER");
    user.setPasswordHash(passwordEncoder.encode(password));

    GameUser salvo = userRepository.save(user);

    System.out.println("SALVO ID: " + salvo.getId());

    return new AuthResponse(
        jwtService.generateToken(
            salvo.getLogin(),
            salvo.getNome(),
            salvo.getRole()
        ),
        salvo.getLogin(),
        salvo.getNome(),
        salvo.getRole()
    );
}

    public AuthResponse login(LoginRequest request) {
        String login = normalize(request.login());
        String password = normalize(request.password());

        if (login.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login e senha sao obrigatorios.");
        }

        GameUser user = userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas."));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas.");
        }

        return new AuthResponse(
                jwtService.generateToken(user.getLogin(), user.getNome(), user.getRole()),
                user.getLogin(),
                user.getNome(),
                user.getRole());
    }

    public void saveAvatar(String login, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem e obrigatoria.");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem deve ter no maximo 5 MB.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie um arquivo de imagem.");
        }

        GameUser user = findUser(login);
        try {
            user.setAvatarData(file.getBytes());
            user.setAvatarContentType(contentType);
            userRepository.save(user);
        } catch (java.io.IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Nao foi possivel salvar a imagem.");
        }
    }

    public GameUser getUser(String login) {
        return findUser(login);
    }

    private void validateRegister(String login, String password, String nome) {
        if (login.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login e senha sao obrigatorios.");
        }

        if (nome.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatorio.");
        }

        if (password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha precisa ter no minimo 6 caracteres.");
        }
    }

    private GameUser findUser(String login) {
        return userRepository.findByLoginIgnoreCase(normalize(login))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao autenticado."));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}