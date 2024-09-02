package com.slava.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class MatchDto {
    private PlayerDto matchWinner;
    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private MatchTypeDto matchType;
    private MatchStateDto matchState;
    private List<SetDto> sets;
    private int player1SetsScore;
    private int player2SetsScore;

//    private Boolean isDeuce;
//    private Boolean isTieBreak;
//    private Boolean isFinished;
}
