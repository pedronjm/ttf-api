package br.cefetmg.comunidadettf.dto.save;

import java.util.List;

public record GameSaveUpsertRequest(
                Integer slotIndex,
                String slotName,
                String selectedCharacter,
                boolean playTutorial,
                String difficulty,
                String sceneName,
                String checkpointId,
                int checkpoint,

                List<String> collectedIds,
                List<String> deadEnemyIds,
                float completionPercent,
                Integer qttAppleCollected,
                Integer qttGlassCollected,
                Integer qttPlasticCollected,
                Integer qttElectronicsCollected,
                Integer qttPaperCollected,
                Integer qttMetalCollected,
                Integer score,

                // ── Campos de teste ──────────────────────────────────────────────────
                Integer currentHealth, // vida atual do jogador
                Integer maxHealth, // vida máxima do personagem
                Integer deathCount // número de mortes acumuladas
// ─────────────────────────────────────────────────────────────────────
) {
}
