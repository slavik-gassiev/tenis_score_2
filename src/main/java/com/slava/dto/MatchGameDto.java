package com.slava.dto;

import java.util.List;

public class MatchGameDto {
    private PlayerDto gameWinner;

    private int player1CurrentScore;
    private int player2CurrentScore;

    private int player1DeuceScore;
    private int player2DeuceScore;

    private Boolean isOngoing;
}
