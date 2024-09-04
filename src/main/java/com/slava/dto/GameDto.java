package com.slava.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameDto {
    private PlayerDto gameWinner;

    private int player1CurrentScore;
    private int player2CurrentScore;

    private DeuceDto deuce;

    private Boolean isOngoing;
}
