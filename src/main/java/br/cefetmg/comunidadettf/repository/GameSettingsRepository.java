package br.cefetmg.comunidadettf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefetmg.comunidadettf.model.game.GameSettings;

@Repository
public interface GameSettingsRepository extends JpaRepository<GameSettings, Long> {

    Optional<GameSettings> findByUsuario_Id(Long usuarioId);

    long deleteByUsuario_Id(Long usuarioId);
}
