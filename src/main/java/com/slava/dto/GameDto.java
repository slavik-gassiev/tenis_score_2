package com.slava.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameDto {
    private PlayerDto gameWinner;

    private int player1CurrentScore;
    private int player2CurrentScore;

    private List<DeuceDto> deuces;

    private Boolean isOngoing;
}
