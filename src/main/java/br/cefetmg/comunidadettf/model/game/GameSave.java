package br.cefetmg.comunidadettf.model.game;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_saves", uniqueConstraints = @UniqueConstraint(
        name = "uq_game_saves_user_slot",
        columnNames = { "user_id", "slot_index" }))
public class GameSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private GameUser usuario;

    @Column(name = "slot_index", nullable = false)
    private Integer slotIndex;

    @Column(name = "slot_name", length = 120, nullable = false)
    private String slotName;

    @Column(name = "selected_character", length = 40, nullable = false)
    private String selectedCharacter;

    @Column(name = "play_tutorial", nullable = false)
    private Boolean playTutorial;

    @Column(length = 40, nullable = false)
    private String difficulty;

    @Column(name = "scene_name", length = 120, nullable = false)
    private String sceneName;

    @Column(name = "checkpoint_id", length = 120, nullable = false)
    private String checkpointId;

    @Column(name = "checkpoint", nullable = false)
    private Integer checkpoint;

    @Column(name = "collected_ids_json", columnDefinition = "JSON", nullable = false)
    private String collectedIdsJson;

    @Column(name = "dead_enemy_ids_json", columnDefinition = "JSON", nullable = false)
    private String deadEnemyIdsJson;

    @Column(name = "completion_percent", nullable = false)
    private Float completionPercent;

    @Column(name = "last_saved_at_utc", nullable = false)
    private LocalDateTime lastSavedAtUtc;

    @Column(name = "qtt_apple_collected", nullable = false)
    private Integer qttAppleCollected;

    @Column(name = "qtt_glass_collected", nullable = false)
    private Integer qttGlassCollected;

    @Column(name = "qtt_plastic_collected", nullable = false)
    private Integer qttPlasticCollected;

    @Column(name = "qtt_electronics_collected", nullable = false)
    private Integer qttElectronicsCollected;

    @Column(name = "qtt_paper_collected", nullable = false)
    private Integer qttPaperCollected;

    @Column(name = "qtt_metal_collected", nullable = false)
    private Integer qttMetalCollected;

    @Column(name = "score", nullable = false)
    private Integer score;

    // ── Campos adicionados para a fase de testes ──────────────────────────────

    /** Vida atual do jogador no momento do save. */
    @Column(name = "current_health", nullable = false)
    private Integer currentHealth = 0;

    /** Vida máxima do personagem (varia por personagem/dificuldade). */
    @Column(name = "max_health", nullable = false)
    private Integer maxHealth = 0;

    /** Número de mortes acumuladas neste save. */
    @Column(name = "death_count", nullable = false)
    private Integer deathCount = 0;

    // ─────────────────────────────────────────────────────────────────────────

    @PrePersist
    @PreUpdate
    public void touch() {
        lastSavedAtUtc = LocalDateTime.now(java.time.ZoneOffset.UTC);
    }
}
