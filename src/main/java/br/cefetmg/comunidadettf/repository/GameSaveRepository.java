package br.cefetmg.comunidadettf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefetmg.comunidadettf.model.game.GameSave;

@Repository
public interface GameSaveRepository extends JpaRepository<GameSave, Long> {

    List<GameSave> findByUsuario_IdOrderBySlotIndexAsc(Long usuarioId);

    Optional<GameSave> findByUsuario_IdAndSlotIndex(Long usuarioId, Integer slotIndex);

    long deleteByUsuario_IdAndSlotIndex(Long usuarioId, Integer slotIndex);
}
