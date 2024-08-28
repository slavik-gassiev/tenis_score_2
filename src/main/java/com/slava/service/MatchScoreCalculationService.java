package com.slava.service;

import com.slava.dto.MatchStateDto;
import com.slava.dto.PlayerDto;

public class MatchScoreCalculationService implements IMatchScoreCalculationService<MatchStateDto, String, PlayerDto>{
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    @Override
    public MatchStateDto toGoal(String matchId, PlayerDto player) {

        checkMatchAndPlayerOnExist(matchId, player);
        checkDeuce(matchId);


        return null;
    }

    private void checkDeuce(String matchId) {

    }

    private void checkMatchAndPlayerOnExist(String matchId, PlayerDto player) {
        Boolean hasMatch = ongoingMatchService.isMatchExist(matchId);
        Boolean isPlayerInMatch = ongoingMatchService.isPlayerInMatch(matchId, player);

        if (!hasMatch) {
            throw new RuntimeException("матч не сушествуют");
        }
        if (!isPlayerInMatch) {
            throw new RuntimeException("игрок не сушествуют");
        }
    }


}
