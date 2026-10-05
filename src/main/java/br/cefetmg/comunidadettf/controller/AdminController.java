package br.cefetmg.comunidadettf.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cefetmg.comunidadettf.service.GameSaveService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final GameSaveService saveService;

    public AdminController(GameSaveService saveService) {
        this.saveService = saveService;
    }

    @GetMapping("/painel")
    public String painel(Principal principal) {
        return "Acesso administrativo autorizado para " + principal.getName();
    }

    @DeleteMapping("/saves/user/{login}")
    public ResponseEntity<Map<String, Object>> deleteUserSaves(@PathVariable String login) {
        long deleted = saveService.deleteAllForUser(login);
        return ResponseEntity.ok(Map.of(
                "message", "Saves do usuario removidos com sucesso.",
                "deleted", deleted));
    }

    @DeleteMapping("/saves")
    public ResponseEntity<Map<String, Object>> deleteAllSaves() {
        long deleted = saveService.deleteAll();
        return ResponseEntity.ok(Map.of(
                "message", "Todos os saves foram removidos com sucesso.",
                "deleted", deleted));
    }
}