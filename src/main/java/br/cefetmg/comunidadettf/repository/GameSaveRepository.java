package br.cefetmg.comunidadettf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.cefetmg.comunidadettf.model.game.GameSave;

@Repository
public interface GameSaveRepository extends JpaRepository<GameSave, Long> {

    List<GameSave> findByUsuario_IdOrderBySlotIndexAsc(Long usuarioId);

    Optional<GameSave> findByUsuario_IdAndSlotIndex(Long usuarioId, Integer slotIndex);

    long deleteByUsuario_IdAndSlotIndex(Long usuarioId, Integer slotIndex);

    /**
     * Leaderboard: para cada usuário, pega o slot com maior score.
     * Retorna os N primeiros ordenados por score desc.
     *
     * A sub-query garante que só um slot por usuário aparece no pódio
     * (o melhor deles).
     */
    @Query(value = """
            SELECT
                u.login            AS login,
                u.nome             AS nome,
                s.score            AS score,
                s.checkpoint_id    AS checkpointId,
                s.completion_percent AS completionPercent,
                s.last_saved_at_utc  AS lastSavedAtUtc
            FROM game_saves s
            JOIN game_users u ON u.id = s.user_id
            WHERE s.score = (
                SELECT MAX(s2.score)
                FROM game_saves s2
                WHERE s2.user_id = s.user_id
            )
            ORDER BY s.score DESC, s.last_saved_at_utc ASC
            LIMIT :limit
            """,
            nativeQuery = true)
    List<LeaderboardRow> findTopPlayersByScore(@Param("limit") int limit);
}
