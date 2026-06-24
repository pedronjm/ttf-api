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
@Table(name = "game_settings", uniqueConstraints = @UniqueConstraint(name = "uq_game_settings_user", columnNames = {
        "user_id" }))
public class GameSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne (em vez de OneToOne) seguindo o mesmo padrao usado em
    // GameSave: evita a complexidade extra de proxy/lazy loading do
    // OneToOne do Hibernate. A unicidade de "1 config por usuario" e
    // garantida pela constraint unique=true abaixo, e nao pelo tipo
    // de relacionamento.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private GameUser usuario;

    // Teclas de movimento e acao.
    // Guardadas como string no formato do binding da Unity (ex.: KeyCode.A,
    // "Space", "LeftArrow", ou o caminho de uma action do Input System como
    // "<Keyboard>/a"). Como o jogador pode remapear para qualquer tecla,
    // o campo fica livre em formato texto em vez de um enum fixo.
    @Column(name = "key_esquerda", length = 60, nullable = false)
    private String keyEsquerda;

    @Column(name = "key_direita", length = 60, nullable = false)
    private String keyDireita;

    @Column(name = "key_dash", length = 60, nullable = false)
    private String keyDash;

    @Column(name = "key_interagir", length = 60, nullable = false)
    private String keyInteragir;

    @Column(name = "key_pular", length = 60, nullable = false)
    private String keyPular;

    @Column(name = "key_melee", length = 60, nullable = false)
    private String keyMelee;

    @Column(name = "key_ranger", length = 60, nullable = false)
    private String keyRanger;

    // Volumes normalizados entre 0.0 e 1.0.
    @Column(name = "volume_geral", nullable = false)
    private Float volumeGeral;

    @Column(name = "volume_musica", nullable = false)
    private Float volumeMusica;

    @Column(name = "volume_sfx", nullable = false)
    private Float volumeSfx;

    @Column(name = "last_saved_at_utc", nullable = false)
    private LocalDateTime lastSavedAtUtc;

    @PrePersist
    @PreUpdate
    public void touch() {
        lastSavedAtUtc = LocalDateTime.now(java.time.ZoneOffset.UTC);
    }
}
