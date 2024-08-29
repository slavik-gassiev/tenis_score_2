package com.slava.dto;

import lombok.Data;

@Data
public class MatchSetDto {
    private PlayerDto winner;

    private int player1GameScore;
    private int player2GameScore;

    private int player1TieBreakScore;
    private int player2TieBreakScore;

    private int player1DeuceScore;
    private int player2DeuceScore;

    private int player1CurrentScore;
    private int player2CurrentScore;

    private Boolean isOngoing;
}
