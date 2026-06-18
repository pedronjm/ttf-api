package br.cefetmg.comunidadettf.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.cefetmg.comunidadettf.dto.save.GameSaveResponse;
import br.cefetmg.comunidadettf.dto.save.GameSaveUpsertRequest;
import br.cefetmg.comunidadettf.model.game.GameSave;
import br.cefetmg.comunidadettf.model.game.GameUser;
import br.cefetmg.comunidadettf.repository.GameSaveRepository;
import br.cefetmg.comunidadettf.repository.GameUserRepository;

@Service
@Transactional
public class GameSaveService {

    private static final int MIN_SLOT_INDEX = 1;
    private static final int MAX_SLOT_INDEX = 3;

    private final GameUserRepository userRepository;
    private final GameSaveRepository saveRepository;
    private final ObjectMapper objectMapper;

    public GameSaveService(
            GameUserRepository userRepository,
            GameSaveRepository saveRepository,
            ObjectMapper objectMapper) {

        this.userRepository = userRepository;
        this.saveRepository = saveRepository;
        this.objectMapper = objectMapper;
    }

    public List<GameSaveResponse> getAll(String login) {
        GameUser user = requireUser(login);

        return saveRepository
                .findByUsuario_IdOrderBySlotIndexAsc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GameSaveResponse getOne(String login, int slotIndex) {
        validateSlotIndex(slotIndex);

        GameUser user = requireUser(login);

        GameSave save = saveRepository
                .findByUsuario_IdAndSlotIndex(user.getId(), slotIndex)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Save nao encontrado."));

        return toResponse(save);
    }

    public GameSaveResponse upsert(String login, GameSaveUpsertRequest request) {
        validateSlotIndex(request.slotIndex());

        GameUser user = requireUser(login);

        GameSave save = saveRepository
                .findByUsuario_IdAndSlotIndex(user.getId(), request.slotIndex())
                .orElse(new GameSave());

        save.setUsuario(user);
        save.setSlotIndex(request.slotIndex());
        save.setSlotName(normalizeOrDefault(request.slotName(), slotLabel(request.slotIndex())));
        save.setSelectedCharacter(normalizeOrDefault(request.selectedCharacter(), "Warrior"));
        save.setPlayTutorial(request.playTutorial());
        save.setDifficulty(normalizeOrDefault(request.difficulty(), "Normal"));
        save.setSceneName(normalizeOrDefault(request.sceneName(), "SampleScene"));
        save.setCheckpointId(normalizeOrDefault(request.checkpointId(), ""));
        save.setCheckpoint(valueInt(request.checkpoint()));
        save.setCollectedIdsJson(toJsonArray(request.collectedIds()));
        save.setDeadEnemyIdsJson(toJsonArray(request.deadEnemyIds()));
        save.setCompletionPercent(valueFloat(request.completionPercent()));
        save.setQttAppleCollected(valueInt(request.qttAppleCollected()));
        save.setQttGlassCollected(valueInt(request.qttGlassCollected()));
        save.setQttPlasticCollected(valueInt(request.qttPlasticCollected()));
        save.setQttElectronicsCollected(valueInt(request.qttElectronicsCollected()));
        save.setQttPaperCollected(valueInt(request.qttPaperCollected()));
        save.setQttMetalCollected(valueInt(request.qttMetalCollected()));
        save.setScore(valueInt(request.score()));

        return toResponse(saveRepository.save(save));
    }

    public boolean delete(String login, int slotIndex) {
        validateSlotIndex(slotIndex);

        GameUser user = requireUser(login);

        return saveRepository
                .deleteByUsuario_IdAndSlotIndex(user.getId(), slotIndex) > 0;
    }

    private GameUser requireUser(String login) {
        String normalizedLogin = normalize(login);

        if (normalizedLogin.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario nao autenticado.");
        }

        return userRepository
                .findByLoginIgnoreCase(normalizedLogin)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario nao autenticado."));
    }

    private void validateSlotIndex(int slotIndex) {
        if (slotIndex < MIN_SLOT_INDEX || slotIndex > MAX_SLOT_INDEX) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Slot invalido. Use um valor entre 1 e 3.");
        }
    }

    private GameSaveResponse toResponse(GameSave save) {
        return new GameSaveResponse(
                save.getSlotIndex(),
                save.getSlotName(),
                save.getSelectedCharacter(),
                Boolean.TRUE.equals(save.getPlayTutorial()),
                save.getDifficulty(),
                save.getSceneName(),
                save.getCheckpointId(),
                save.getCheckpoint(),
                save.getCollectedIdsJson(),
                save.getDeadEnemyIdsJson(),
                save.getCompletionPercent(),
                save.getQttAppleCollected(),
                save.getQttGlassCollected(),
                save.getQttPlasticCollected(),
                save.getQttElectronicsCollected(),
                save.getQttPaperCollected(),
                save.getQttMetalCollected(),
                save.getScore(),
                save.getLastSavedAtUtc() == null
                        ? null
                        : save.getLastSavedAtUtc().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeOrDefault(String value, String defaultValue) {
        String result = normalize(value);
        return result.isEmpty() ? defaultValue : result;
    }

    private String slotLabel(int slotIndex) {
        return "Slot " + slotIndex;
    }

    private String toJsonArray(List<String> values) {
        try {
            return objectMapper.writeValueAsString(values == null ? List.of() : values);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro convertendo JSON do save.");
        }
    }

    private Integer valueInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Float valueFloat(Float value) {
        return value == null ? 0 : value;
    }
}
