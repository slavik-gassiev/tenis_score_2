package com.slava.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchSetDto {
    private PlayerDto winner;

//    вынести в класс
    private int player1GameScore;
    private int player2GameScore;
    private List<MatchGameDto> games;

    private int player1TieBreakScore;
    private int player2TieBreakScore;
    private List<TieBreakScoreDto> tieBreaks;

    private int player1DeuceScore;
    private int player2DeuceScore;

    private int player1CurrentScore;
    private int player2CurrentScore;

    private Boolean isOngoing;
}
