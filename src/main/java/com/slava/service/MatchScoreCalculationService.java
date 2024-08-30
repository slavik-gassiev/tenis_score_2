package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.MatchSetDto;
import com.slava.dto.MatchStateDto;
import com.slava.dto.MatchTypeDto;
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

        checkDeuce();
        checkTieBreak();
        checkSetWinner();
        checkMatchFinished();

        addPoint(player);

        return null;
    }

    private void addPoint(PlayerDto player) {
        if (match.getIsDeuce()) {
            addDeucePoint(player);
        }
        else if (match.getIsTieBreak()) {
            addTieBreakPoint(player);
        }
        else {
            addScorePoint(player);
        }
    }

    private void addScorePoint(PlayerDto player) {
        Boolean isPlayer1 = match.getPlayerOne().getName() == player.getName() ? true : false;
        int playerScoreWinner = isPlayer1 ? set.getPlayer1CurrentScore() : set.getPlayer2CurrentScore();
        int playerScoreLoser = isPlayer1 ? set.getPlayer2CurrentScore() : set.getPlayer1CurrentScore();

        switch (playerScoreWinner) {
            case 0:
                playerScoreWinner = 15;
                break;
            case 15:
                playerScoreWinner = 30;
                break;
            case 30:
                if (40 == playerScoreLoser) {
                    match.setIsDeuce(true);
                    resetScore(set);
                } else {
                    playerScoreWinner = 40;
                }
                break;
            case 40:
                if (isPlayer1) {
                    set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);
                    resetScore(set);
                } else {
                    set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
                    resetScore(set);
                }
                return;

        }
    }

    private void addTieBreakPoint(PlayerDto player) {
        if (match.getPlayerOne().getName() == player.getName()) {
            set.setPlayer1TieBreakScore(set.getPlayer1TieBreakScore() + 1);
        }
        else if (match.getPlayerTwo().getName() == player.getName()) {
            set.setPlayer2TieBreakScore(set.getPlayer2TieBreakScore() + 1);
        }
    }

    private void addDeucePoint(PlayerDto player) {
        if (match.getPlayerOne().getName() == player.getName()) {
            set.setPlayer1DeuceScore(set.getPlayer1DeuceScore() + 1);
        }
        else if (match.getPlayerTwo().getName() == player.getName()) {
            set.setPlayer2DeuceScore(set.getPlayer2DeuceScore() + 1);
        }
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

    private void checkDeuce() {
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

    private void checkTieBreak() {
        if (set.getPlayer1TieBreakScore() >= 7 &&
                set.getPlayer1TieBreakScore() - set.getPlayer2TieBreakScore() >= 2) {
            set.setPlayer1TieBreakScore(set.getPlayer1TieBreakScore() + 1);
            resetTieBreak(set);
            resetGame(set);
        }
        else if (set.getPlayer2TieBreakScore() >= 2 &&
                set.getPlayer2TieBreakScore() - set.getPlayer1TieBreakScore() >= 2) {
            set.setPlayer2TieBreakScore(set.getPlayer2TieBreakScore() + 1);
            resetTieBreak(set);
            resetGame(set);
        }
    }

    private void checkSetWinner() {
        if (set.getPlayer1GameScore() >= 6 &&
                set.getPlayer1GameScore() - set.getPlayer2GameScore() >= 2) {
            set.setWinner(match.getPlayerOne());
            finishSet();
        }
        else if (set.getPlayer2GameScore() >= 6 &&
                set.getPlayer2GameScore() - set.getPlayer1GameScore() >= 2) {
            set.setWinner(match.getPlayerTwo());
            finishSet();
        }
        else if (set.getPlayer1GameScore() == 6 &&
                set.getPlayer2GameScore() == 6) {
            match.setIsTieBreak(true);
            resetGame(set);
            resetScore(set);
        }
    }

    private void checkMatchFinished() {
        int playerOneSets = (int) match.getSets().stream()
                .filter(matchSetDto -> matchSetDto.getWinner() == match.getPlayerOne())
                .count();

        int playerTwoSets = (int) match.getSets().stream()
                .filter(matchSetDto -> matchSetDto.getWinner() == match.getPlayerTwo())
                .count();

        int goal = match.getMatchTypeDto() == MatchTypeDto.SHORT_GAME ? 3 : 6;

        if (playerOneSets == goal){
            match.setMatchWinner(match.getPlayerOne());
            match.setIsFinished(true);
        }
        else if (playerTwoSets == goal) {
            match.setMatchWinner(match.getPlayerTwo());
            match.setIsFinished(true);
        }
    }

    private void finishSet() {
        set.setIsOngoing(false);
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

    private void resetTieBreak(MatchSetDto set) {
        set.setPlayer1TieBreakScore(0);
        set.setPlayer2TieBreakScore(0);
        match.setIsTieBreak(false);
    }

    private void resetGame(MatchSetDto set) {
        set.setPlayer1GameScore(0);
        set.setPlayer2GameScore(0);
    }


}
