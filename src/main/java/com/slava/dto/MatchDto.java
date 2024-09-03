package com.slava.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
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
}
