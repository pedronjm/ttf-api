package br.cefetmg.comunidadettf.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.cefetmg.comunidadettf.dto.leaderboard.LeaderboardEntry;
import br.cefetmg.comunidadettf.service.LeaderboardService;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Retorna o pódio dos jogadores ordenado por maior pontuação.
     *
     * GET /leaderboard          → top 10
     * GET /leaderboard?top=50   → top N (máximo 100)
     *
     * Endpoint PÚBLICO — não exige autenticação.
     */
    @GetMapping
    public List<LeaderboardEntry> getLeaderboard(
            @RequestParam(defaultValue = "10") int top) {

        int limit = Math.min(Math.max(top, 1), 100);
        return leaderboardService.getTopPlayers(limit);
    }
}
