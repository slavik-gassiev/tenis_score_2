package com.slava.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@EqualsAndHashCode
public class MatchDto {
    @JsonIgnore
    private PlayerDto matchWinner;
    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private MatchTypeDto matchType;
    private MatchStateDto matchState;
    private List<SetDto> sets;
    private int player1SetsScore;
    private int player2SetsScore;

    public void deleteOngoingSet() {
        this.sets.removeIf(setDto -> setDto.getIsOngoing() == true);
    }
    public SetDto getOngoingSet() {
        Optional<SetDto> set = sets.stream().filter(SetDto::getIsOngoing).findFirst();
        if (set.isPresent()) {
            return set.get();
        } else {
            throw new RuntimeException("Все сеты завершены");
        }
    }

}
