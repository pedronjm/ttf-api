package br.cefetmg.comunidadettf.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetmg.comunidadettf.dto.leaderboard.LeaderboardEntry;
import br.cefetmg.comunidadettf.repository.GameSaveRepository;

@Service
@Transactional(readOnly = true)
public class LeaderboardService {

    private final GameSaveRepository saveRepository;

    public LeaderboardService(GameSaveRepository saveRepository) {
        this.saveRepository = saveRepository;
    }

    /**
     * Retorna os N melhores jogadores baseados no maior score
     * entre todos os slots de cada usuário.
     */
    public List<LeaderboardEntry> getTopPlayers(int limit) {
        var rows = saveRepository.findTopPlayersByScore(limit);

        var entries = new ArrayList<LeaderboardEntry>();
        for (int i = 0; i < rows.size(); i++) {
            var row = rows.get(i);

            String lastSaved = row.getLastSavedAtUtc() == null
                    ? null
                    : row.getLastSavedAtUtc().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            entries.add(new LeaderboardEntry(
                    i + 1,
                    row.getLogin(),
                    row.getNome(),
                    row.getScore(),
                    row.getCheckpointId(),
                    row.getCompletionPercent(),
                    lastSaved
            ));
        }

        return entries;
    }
}
