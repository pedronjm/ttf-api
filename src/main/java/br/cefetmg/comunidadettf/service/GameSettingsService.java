package br.cefetmg.comunidadettf.service;

import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.cefetmg.comunidadettf.dto.settings.GameSettingsResponse;
import br.cefetmg.comunidadettf.dto.settings.GameSettingsUpsertRequest;
import br.cefetmg.comunidadettf.model.game.GameSettings;
import br.cefetmg.comunidadettf.model.game.GameUser;
import br.cefetmg.comunidadettf.repository.GameSettingsRepository;
import br.cefetmg.comunidadettf.repository.GameUserRepository;

@Service
@Transactional
public class GameSettingsService {

    // Valores padrao usados quando o jogador ainda nao tem configuracoes
    // salvas (primeiro acesso) ou quando um campo especifico chega vazio.
    private static final String DEFAULT_KEY_ESQUERDA = "<Keyboard>/a";
    private static final String DEFAULT_KEY_DIREITA = "<Keyboard>/d";
    private static final String DEFAULT_KEY_DASH = "<Keyboard>/leftShift";
    private static final String DEFAULT_KEY_INTERAGIR = "<Keyboard>/e";
    private static final String DEFAULT_KEY_PULAR = "<Keyboard>/space";
    private static final String DEFAULT_KEY_MELEE = "<Mouse>/rightButton";
    private static final String DEFAULT_KEY_RANGER = "<Mouse>/leftButton";

    private static final float DEFAULT_VOLUME_GERAL = 1f;
    private static final float DEFAULT_VOLUME_MUSICA = 1f;
    private static final float DEFAULT_VOLUME_SFX = 1f;

    private final GameUserRepository userRepository;
    private final GameSettingsRepository settingsRepository;

    public GameSettingsService(
            GameUserRepository userRepository,
            GameSettingsRepository settingsRepository) {

        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
    }

    public GameSettingsResponse getOrDefault(String login) {
        GameUser user = requireUser(login);

        return settingsRepository
                .findByUsuario_Id(user.getId())
                .map(this::toResponse)
                .orElseGet(this::defaultResponse);
    }

    public GameSettingsResponse upsert(String login, GameSettingsUpsertRequest request) {
        GameUser user = requireUser(login);

        GameSettings settings = settingsRepository
                .findByUsuario_Id(user.getId())
                .orElse(new GameSettings());

        settings.setUsuario(user);
        settings.setKeyEsquerda(normalizeOrDefault(request.keyEsquerda(), DEFAULT_KEY_ESQUERDA));
        settings.setKeyDireita(normalizeOrDefault(request.keyDireita(), DEFAULT_KEY_DIREITA));
        settings.setKeyDash(normalizeOrDefault(request.keyDash(), DEFAULT_KEY_DASH));
        settings.setKeyInteragir(normalizeOrDefault(request.keyInteragir(), DEFAULT_KEY_INTERAGIR));
        settings.setKeyPular(normalizeOrDefault(request.keyPular(), DEFAULT_KEY_PULAR));
        settings.setKeyMelee(normalizeOrDefault(request.keyMelee(), DEFAULT_KEY_MELEE));
        settings.setKeyRanger(normalizeOrDefault(request.keyRanger(), DEFAULT_KEY_RANGER));
        settings.setVolumeGeral(clampVolume(request.volumeGeral(), DEFAULT_VOLUME_GERAL));
        settings.setVolumeMusica(clampVolume(request.volumeMusica(), DEFAULT_VOLUME_MUSICA));
        settings.setVolumeSfx(clampVolume(request.volumeSfx(), DEFAULT_VOLUME_SFX));

        return toResponse(settingsRepository.save(settings));
    }

    public boolean resetToDefault(String login) {
        GameUser user = requireUser(login);

        return settingsRepository.deleteByUsuario_Id(user.getId()) > 0;
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

    private GameSettingsResponse toResponse(GameSettings settings) {
        return new GameSettingsResponse(
                settings.getKeyEsquerda(),
                settings.getKeyDireita(),
                settings.getKeyDash(),
                settings.getKeyInteragir(),
                settings.getKeyPular(),
                settings.getKeyMelee(),
                settings.getKeyRanger(),
                settings.getVolumeGeral(),
                settings.getVolumeMusica(),
                settings.getVolumeSfx(),
                settings.getLastSavedAtUtc() == null
                        ? null
                        : settings.getLastSavedAtUtc().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    private GameSettingsResponse defaultResponse() {
        return new GameSettingsResponse(
                DEFAULT_KEY_ESQUERDA,
                DEFAULT_KEY_DIREITA,
                DEFAULT_KEY_DASH,
                DEFAULT_KEY_INTERAGIR,
                DEFAULT_KEY_PULAR,
                DEFAULT_KEY_MELEE,
                DEFAULT_KEY_RANGER,
                DEFAULT_VOLUME_GERAL,
                DEFAULT_VOLUME_MUSICA,
                DEFAULT_VOLUME_SFX,
                null
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeOrDefault(String value, String defaultValue) {
        String result = normalize(value);
        return result.isEmpty() ? defaultValue : result;
    }

    private Float clampVolume(Float value, float defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (value < 0f) {
            return 0f;
        }

        if (value > 1f) {
            return 1f;
        }

        return value;
    }
}
