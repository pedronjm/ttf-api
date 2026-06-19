package br.cefetmg.comunidadettf.repository;

import java.time.LocalDateTime;

/**
 * Projeção retornada pela query de leaderboard.
 * Cada linha representa o melhor save de um usuário.
 */
public interface LeaderboardRow {
    String        getLogin();
    String        getNome();
    Integer       getScore();
    String        getCheckpointId();
    Float         getCompletionPercent();
    LocalDateTime getLastSavedAtUtc();
}
