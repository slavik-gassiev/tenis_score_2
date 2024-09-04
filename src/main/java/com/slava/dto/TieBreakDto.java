package com.slava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TieBreakDto {
    private PlayerDto tieBreakWinner;

    private int player1TieBreakScore;
    private int player2TieBreakScore;

    private Boolean isOngoing;
}
