package com.slava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeuceDto {
    private PlayerDto deuceWinner;

    private int player1DeuceScore;
    private int player2DeuceScore;

    private Boolean isOngoing;
}
