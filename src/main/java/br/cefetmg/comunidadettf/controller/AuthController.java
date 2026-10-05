package br.cefetmg.comunidadettf.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;

import br.cefetmg.comunidadettf.dto.auth.AuthResponse;
import br.cefetmg.comunidadettf.dto.auth.LoginRequest;
import br.cefetmg.comunidadettf.dto.auth.RegisterRequest;
import br.cefetmg.comunidadettf.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadAvatar(Principal principal, @RequestPart("file") MultipartFile file) {
        authService.saveAvatar(principal.getName(), file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar(Principal principal) {
        var user = authService.getUser(principal.getName());
        if (user.getAvatarData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(user.getAvatarContentType()))
                .body(user.getAvatarData());
    }
}