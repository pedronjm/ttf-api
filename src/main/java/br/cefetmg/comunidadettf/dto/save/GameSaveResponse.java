package br.cefetmg.comunidadettf.dto.save;

public record GameSaveResponse(
        Integer slotIndex,
        String slotName,
        String selectedCharacter,
        boolean playTutorial,
        String difficulty,
        String sceneName,
        String checkpointId,
        int checkpoint,

        String collectedIdsJson,
        String deadEnemyIdsJson,
        float completionPercent,
        Integer qttAppleCollected,
        Integer qttGlassCollected,
        Integer qttPlasticCollected,
        Integer qttElectronicsCollected,
        Integer qttPaperCollected,
        Integer qttMetalCollected,
        Integer score,

        // ── Campos de teste ──────────────────────────────────────────────────
        Integer currentHealth,
        Integer maxHealth,
        Integer deathCount,
        // ─────────────────────────────────────────────────────────────────────

        String lastSavedAtUtc
) {}
