package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.MatchSetDto;
import com.slava.dto.MatchStateDto;
import com.slava.dto.PlayerDto;

import java.util.Optional;

public class MatchScoreCalculationService implements IMatchScoreCalculationService<MatchStateDto, String, PlayerDto>{
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private OngoingMatchDAO ongoingMatchDAO = new OngoingMatchDAO();
    private volatile MatchStateDto match;
    private volatile MatchSetDto set;
    @Override
    public MatchStateDto toGoal(String matchId, PlayerDto player) {

        checkMatchAndPlayerOnExist(matchId, player);
        initMatchAndSetDto(matchId);
        checkDeuce(matchId);


        return null;
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

    private void initMatchAndSetDto(String matchId) {
        Optional<MatchStateDto> optionalMatch = ongoingMatchDAO.getMatchByUUID(matchId);

        if (optionalMatch.isPresent()) {
            match = optionalMatch.get();
        }

        Optional<MatchSetDto> optionalMatchSetDto = match.getSets().stream()
                .filter(matchSetDto1 -> set.getIsOngoing())
                .findFirst();

        if (optionalMatchSetDto.isPresent()) {
            set = optionalMatchSetDto.get();
        }
    }

    private void checkDeuce(String matchId) {
        if (set.getPlayer1DeuceScore() >= 2 &&
                set.getPlayer1DeuceScore() - set.getPlayer2DeuceScore() >= 2) {
            set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);
            resetDeuce(set);
            resetScore(set);
        }
        else if (set.getPlayer2DeuceScore() >= 2 &&
                set.getPlayer2DeuceScore() - set.getPlayer1DeuceScore() >= 2) {
            set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
            resetDeuce(set);
            resetScore(set);
        }
    }

    private void resetScore(MatchSetDto set) {
        set.setPlayer1CurrentScore(0);
        set.setPlayer2CurrentScore(0);
    }

    private void resetDeuce(MatchSetDto set) {
        set.setPlayer1DeuceScore(0);
        set.setPlayer2DeuceScore(0);
        match.setIsDeuce(false);
    }


}
