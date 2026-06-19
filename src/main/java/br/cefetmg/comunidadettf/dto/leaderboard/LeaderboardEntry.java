package br.cefetmg.comunidadettf.dto.leaderboard;

public record LeaderboardEntry(
        int    position,
        String login,
        String nome,
        int    score,
        String checkpointId,
        float  completionPercent,
        String lastSavedAtUtc
) {}
