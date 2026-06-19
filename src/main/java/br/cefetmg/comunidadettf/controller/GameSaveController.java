package br.cefetmg.comunidadettf.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.cefetmg.comunidadettf.dto.save.GameSaveResponse;
import br.cefetmg.comunidadettf.dto.save.GameSaveUpsertRequest;
import br.cefetmg.comunidadettf.service.GameSaveService;

@RestController
@RequestMapping("/saves")
public class GameSaveController {

        private final GameSaveService saveService;

        public GameSaveController(GameSaveService saveService) {

                this.saveService = saveService;

        }

        @GetMapping
        public List<GameSaveResponse> getAll(
                        Principal principal) {

                return saveService.getAll(
                                principal.getName());

        }

        @GetMapping("/{slotIndex}")
        public GameSaveResponse getOne(
                        Principal principal,
                        @PathVariable int slotIndex) {

                return saveService.getOne(
                                principal.getName(),
                                slotIndex);

        }

        @PutMapping
        public GameSaveResponse save(
                        Principal principal,
                        @RequestBody GameSaveUpsertRequest request) {

                System.out.println(
                                "PRINCIPAL: "
                                                + principal);

                return saveService.upsert(
                                principal.getName(),
                                request);
        }

        @DeleteMapping("/{slotIndex}")
        public ResponseEntity<Void> delete(
                        Principal principal,
                        @PathVariable int slotIndex) {

                boolean deleted = saveService.delete(
                                principal.getName(),
                                slotIndex);

                return deleted
                                ? ResponseEntity.noContent().build()
                                : ResponseEntity.notFound().build();

        }

}