package br.cefetmg.comunidadettf.dto.settings;

public record GameSettingsResponse(
        String keyEsquerda,
        String keyDireita,
        String keyDash,
        String keyInteragir,
        String keyPular,
        String keyMelee,
        String keyRanger,

        Float volumeGeral,
        Float volumeMusica,
        Float volumeSfx,

        String lastSavedAtUtc
) {
}
