package com.slava.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class MatchStateDto {
    private PlayerDto matchWinner;
    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private MatchTypeDto matchTypeDto;
    private List<MatchSetDto> sets;
    private Boolean isDeuce;
    private Boolean isTieBreak;
    private Boolean isFinished;
}
