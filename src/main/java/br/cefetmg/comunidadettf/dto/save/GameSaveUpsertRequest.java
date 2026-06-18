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
        Integer score
) {
}
