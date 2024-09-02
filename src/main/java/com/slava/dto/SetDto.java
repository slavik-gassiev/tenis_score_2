package com.slava.dto;

import lombok.Data;

import java.util.List;

@Data
public class SetDto {
    private PlayerDto setWinner;

    private List<GameDto> games;
    private List<TieBreakDto> tieBreaks;

    private int player1GameScore;
    private int player2GameScore;

    private int player1TieBreakScore;
    private int player2TieBreakScore;

    private Boolean isOngoing;
}
