package com.slava.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class SetDto {
    private PlayerDto setWinner;
    private List<GameDto> games;
    private TieBreakDto tieBreak;
    private int player1GameScore;
    private int player2GameScore;
    private Boolean isOngoing;

    public void deleteOngoingGame() {
        this.games.removeIf(gameDto -> gameDto.getIsOngoing() == true);
    }
    public GameDto getOngoingGame() {
         Optional<GameDto> game = games.stream().filter(GameDto::getIsOngoing).findFirst();
         if (game.isPresent()) {
             return game.get();
         } else {
             throw new RuntimeException("Все геймы завершины");
         }
    }
}
