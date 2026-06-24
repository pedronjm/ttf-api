package br.cefetmg.comunidadettf.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.cefetmg.comunidadettf.dto.settings.GameSettingsResponse;
import br.cefetmg.comunidadettf.dto.settings.GameSettingsUpsertRequest;
import br.cefetmg.comunidadettf.service.GameSettingsService;

@RestController
@RequestMapping("/settings")
public class GameSettingsController {

        private final GameSettingsService settingsService;

        public GameSettingsController(GameSettingsService settingsService) {

                this.settingsService = settingsService;

        }

        @GetMapping
        public GameSettingsResponse get(
                        Principal principal) {

                return settingsService.getOrDefault(
                                principal.getName());

        }

        @PutMapping
        public GameSettingsResponse save(
                        Principal principal,
                        @RequestBody GameSettingsUpsertRequest request) {

                return settingsService.upsert(
                                principal.getName(),
                                request);

        }

        @DeleteMapping
        public ResponseEntity<Void> reset(
                        Principal principal) {

                boolean deleted = settingsService.resetToDefault(
                                principal.getName());

                return deleted
                                ? ResponseEntity.noContent().build()
                                : ResponseEntity.notFound().build();

        }

}
