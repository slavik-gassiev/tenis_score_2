package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.*;
import com.slava.service.interfaces.IMatchScoreCalculationService;

public class MatchScoreCalculationService implements IMatchScoreCalculationService<MatchDto, String, PlayerDto> {
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private NewMatchService newMatchService = new NewMatchService();
    private OngoingMatchDAO ongoingMatchDAO = new OngoingMatchDAO();
    @Override
    public MatchDto toGoal(String matchId, PlayerDto player) {

        checkMatchAndPlayerOnExist(matchId, player);

        MatchDto match = ongoingMatchService.getMatch(matchId);

        addPoint(player, match);

        checkDeuce(match);
        checkTieBreak(match);
        if (match.getMatchState() == MatchStateDto.ONGOING) {
            checkSetWinner(match);
        }

        return match;
    }

    private synchronized void addPoint(PlayerDto player, MatchDto match) {
        if (match.getMatchState() == MatchStateDto.DEUCE) {
            addDeucePoint(player, match);
        }
        else if (match.getMatchState() == MatchStateDto.TIE_BREAK) {
            addTieBreakPoint(player, match);
        }
        else {
            addScorePoint(player, match);
        }
    }

    private  void addScorePoint(PlayerDto player, MatchDto match) {
        SetDto set = match.getOngoingSet();
        GameDto game = set.getOngoingGame();
        Boolean isPlayer1 = match.getPlayerOne().getName() == player.getName() ? true : false;
        int playerScoreWinner = isPlayer1 ? game.getPlayer1CurrentScore() : game.getPlayer2CurrentScore();
        int playerScoreLoser = isPlayer1 ? game.getPlayer2CurrentScore() : game.getPlayer1CurrentScore();

        switch (playerScoreWinner) {
            case 0:
                if (isPlayer1) {
                    game.setPlayer1CurrentScore(game.getPlayer1CurrentScore() + 15);
                }
                else {
                    game.setPlayer2CurrentScore(game.getPlayer2CurrentScore() + 15);
                }
                break;
            case 15:
                if (isPlayer1) {
                    game.setPlayer1CurrentScore(game.getPlayer1CurrentScore() + 15);
                }
                else {
                    game.setPlayer2CurrentScore(game.getPlayer2CurrentScore() + 15);
                }
                break;
            case 30:
                if (isPlayer1) {
                    game.setPlayer1CurrentScore(game.getPlayer1CurrentScore() + 10);
                }
                else {
                    game.setPlayer2CurrentScore(game.getPlayer2CurrentScore() + 10);
                }
                if (40 == playerScoreLoser) {
                    match.setMatchState(MatchStateDto.DEUCE);
                    return;
                }
                break;
            case 40:
                addGameWinner(match, isPlayer1);
                return;

        }
    }

    private void addGameWinner(MatchDto match, Boolean isPlayer1) {
        SetDto set = match.getOngoingSet();
        GameDto game = set.getOngoingGame();
        if (isPlayer1) {
            game.setGameWinner(match.getPlayerOne());
            game.setIsOngoing(false);
            set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);
            set.getGames().add(newMatchService.initGame(newMatchService.initDeuce()));

        } else {
            game.setGameWinner(match.getPlayerTwo());
            game.setIsOngoing(false);
            set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
            set.getGames().add(newMatchService.initGame(newMatchService.initDeuce()));
        }
    }

    private synchronized void addTieBreakPoint(PlayerDto player, MatchDto match) {
        TieBreakDto tieBreak = match.getOngoingSet().getTieBreak();
        if (match.getPlayerOne().getName() == player.getName()) {
            tieBreak.setPlayer1TieBreakScore(tieBreak.getPlayer1TieBreakScore() + 1);
        }
        else if (match.getPlayerTwo().getName() == player.getName()) {
            tieBreak.setPlayer2TieBreakScore(tieBreak.getPlayer2TieBreakScore() + 1);
        }
    }

    private synchronized void addDeucePoint(PlayerDto player, MatchDto match) {
        DeuceDto deuce = match.getOngoingSet().getOngoingGame().getDeuce();
        if (match.getPlayerOne().getName() == player.getName()) {
            deuce.setPlayer1DeuceScore(deuce.getPlayer1DeuceScore() + 1);
        }
        else if (match.getPlayerTwo().getName() == player.getName()) {
            deuce.setPlayer2DeuceScore(deuce.getPlayer2DeuceScore() + 1);
        }
    }

    private synchronized void checkMatchAndPlayerOnExist(String matchId, PlayerDto player) {
        Boolean hasMatch = ongoingMatchService.isMatchExist(matchId);
        Boolean isPlayerInMatch = ongoingMatchService.isPlayerInMatch(matchId, player);

        if (!hasMatch) {
            throw new RuntimeException("матч не сушествуют");
        }
        if (!isPlayerInMatch) {
            throw new RuntimeException("игрок не сушествуют");
        }
    }



    private  void checkDeuce(MatchDto match) {
        SetDto set = match.getOngoingSet();
        GameDto game = set.getOngoingGame();
        DeuceDto deuce = set.getOngoingGame().getDeuce();
        if (deuce.getPlayer1DeuceScore() >= 2 &&
                deuce.getPlayer1DeuceScore() - deuce.getPlayer2DeuceScore() >= 2) {

            deuce.setDeuceWinner(match.getPlayerOne());
            game.setGameWinner(match.getPlayerOne());
            game.setIsOngoing(false);
            set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);
            match.setMatchState(MatchStateDto.ONGOING);
        }
        else if (deuce.getPlayer2DeuceScore() >= 2 &&
                deuce.getPlayer2DeuceScore() - deuce.getPlayer1DeuceScore() >= 2) {

            deuce.setDeuceWinner(match.getPlayerTwo());
            game.setGameWinner(match.getPlayerTwo());
            game.setIsOngoing(false);
            set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
            match.setMatchState(MatchStateDto.ONGOING);
        }
    }

    private synchronized void checkTieBreak(MatchDto match) {
        SetDto set = match.getOngoingSet();
        TieBreakDto tieBreak = set.getTieBreak();
        if (tieBreak.getPlayer1TieBreakScore() >= 7 &&
                tieBreak.getPlayer1TieBreakScore() - tieBreak.getPlayer2TieBreakScore() >= 2) {

            tieBreak.setTieBreakWinner(match.getPlayerOne());
            set.setSetWinner(match.getPlayerOne());
            set.setIsOngoing(false);
            match.setPlayer1SetsScore(match.getPlayer1SetsScore() + 1);
            checkMatchFinished(match);
        }
        else if (tieBreak.getPlayer2TieBreakScore() >= 7 &&
                tieBreak.getPlayer2TieBreakScore() - tieBreak.getPlayer1TieBreakScore() >= 2) {

            tieBreak.setTieBreakWinner(match.getPlayerTwo());
            set.setSetWinner(match.getPlayerTwo());
            set.setIsOngoing(false);
            match.setPlayer2SetsScore(match.getPlayer2SetsScore() + 1);
            checkMatchFinished(match);
        }
    }

    private  void checkSetWinner(MatchDto match) {
        SetDto set = match.getOngoingSet();
        if (set.getPlayer1GameScore() >= 6 &&
                set.getPlayer1GameScore() - set.getPlayer2GameScore() >= 2) {
            set.setSetWinner(match.getPlayerOne());
            match.setPlayer1SetsScore(match.getPlayer1SetsScore() + 1);
            set.setIsOngoing(false);
            checkMatchFinished(match);
        }
        else if (set.getPlayer2GameScore() >= 6 &&
                set.getPlayer2GameScore() - set.getPlayer1GameScore() >= 2) {
            set.setSetWinner(match.getPlayerTwo());
            match.setPlayer2SetsScore(match.getPlayer2SetsScore() + 1);
            set.setIsOngoing(false);
            checkMatchFinished(match);
        }
        else if (set.getPlayer1GameScore() == 6 &&
                set.getPlayer2GameScore() == 6) {
            match.setMatchState(MatchStateDto.TIE_BREAK);
        }
    }

    private synchronized void checkMatchFinished(MatchDto match) {
        int goal = match.getMatchType() == MatchTypeDto.SHORT_GAME ? 2 : 2;

        if (match.getPlayer1SetsScore() == goal){
            match.setMatchWinner(match.getPlayerOne());
            match.setMatchState(MatchStateDto.FINISHED);
        }
        else if (match.getPlayer2SetsScore() == goal) {
            match.setMatchWinner(match.getPlayerTwo());
            match.setMatchState(MatchStateDto.FINISHED);
        }
        else {
            match.getSets().add(newMatchService.initSet());
            match.setMatchState(MatchStateDto.ONGOING);
        }
    }
}
