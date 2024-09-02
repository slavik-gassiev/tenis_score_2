package com.slava.dto;

import lombok.Data;

@Data
public class TieBreakDto {
    private PlayerDto tieBreakWinner;

    private int player1TieBreakScore;
    private int player2TieBreakScore;

    private Boolean isOngoing;
}
