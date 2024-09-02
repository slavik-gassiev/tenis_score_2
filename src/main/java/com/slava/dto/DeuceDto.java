package com.slava.dto;

import lombok.Data;

@Data
public class DeuceDto {
    private PlayerDto deuceWinner;

    private int player1DeuceScore;
    private int player2DeuceScore;

    private Boolean isOngoing;
}
