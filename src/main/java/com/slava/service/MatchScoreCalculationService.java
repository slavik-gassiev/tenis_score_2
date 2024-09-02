package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.*;

import java.util.Optional;

public class MatchScoreCalculationService implements IMatchScoreCalculationService<MatchStateDto, String, PlayerDto>{
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private OngoingMatchDAO ongoingMatchDAO = new OngoingMatchDAO();
//    volatile MatchStateDto match;
//    volatile MatchSetDto set;
    //рейс кондишейн
    @Override
    public MatchStateDto toGoal(String matchId, PlayerDto player) {

        checkMatchAndPlayerOnExist(matchId, player);

        MatchStateDto match = null;
        SetDto set = null;
        GameDto game = null;
        DeuceDto deuce = null;
        TieBreakDto tieBreak = null;

        initMatchSetGameDeuceTieBreak(matchId, match, set, game, deuce, tieBreak);

        checkDeuce(match, set, game, deuce);
        checkTieBreak(match, set, tieBreak);
        checkSetWinner(match, set);
        checkMatchFinished(match);

        addPoint(player, match, set, game, tieBreak, deuce);

        return null;
    }

    private synchronized void addPoint(PlayerDto player, MatchStateDto match, SetDto set, GameDto game, TieBreakDto tieBreak, DeuceDto deuce) {
        if (match.getIsDeuce()) {
            addDeucePoint(player, match, deuce);
        }
        else if (match.getIsTieBreak()) {
            addTieBreakPoint(player, match, tieBreak);
        }
        else {
            addScorePoint(player, match, set, game);
        }
    }

    private synchronized void addScorePoint(PlayerDto player, MatchStateDto match, SetDto set, GameDto game) {
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
                    game.setPlayer1CurrentScore(game.getPlayer1CurrentScore() + 30);
                }
                else {
                    game.setPlayer2CurrentScore(game.getPlayer2CurrentScore() + 30);
                }
                break;
            case 30:
                if (40 == playerScoreLoser) {
                    match.setIsDeuce(true);
                    return;
                }
                if (isPlayer1) {
                    game.setPlayer1CurrentScore(game.getPlayer1CurrentScore() + 15);
                }
                else {
                    game.setPlayer2CurrentScore(game.getPlayer2CurrentScore() + 15);
                }
                break;
            case 40:
                if (isPlayer1) {
                    game.setGameWinner(match.getPlayerOne());
                    game.setIsOngoing(false);
                    set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);

                } else {
                    game.setGameWinner(match.getPlayerTwo());
                    game.setIsOngoing(false);
                    set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
                }
                return;

        }
    }

    private synchronized void addTieBreakPoint(PlayerDto player, MatchStateDto match, TieBreakDto tieBreak) {
        if (match.getPlayerOne().getName() == player.getName()) {
            tieBreak.setPlayer1TieBreakScore(tieBreak.getPlayer1TieBreakScore() + 1);
        }
        else if (match.getPlayerTwo().getName() == player.getName()) {
            tieBreak.setPlayer2TieBreakScore(tieBreak.getPlayer2TieBreakScore() + 1);
        }
    }

    private synchronized void addDeucePoint(PlayerDto player, MatchStateDto match, DeuceDto deuce) {
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

    private synchronized void initMatchSetGameDeuceTieBreak(
            String matchId, MatchStateDto match, SetDto set, GameDto game, DeuceDto deuce, TieBreakDto tieBreak
    ) {
        Optional<MatchStateDto> optionalMatch = ongoingMatchDAO.getMatchByUUID(matchId);

        if (optionalMatch.isPresent()) {
            match = optionalMatch.get();
        } else {
            throw new RuntimeException("Данный матч не сушествует");
        }

        Optional<SetDto> optionalMatchSetDto = match.getSets().stream()
                .filter(setDto1 -> setDto1.getIsOngoing() == true)
                .findFirst();

        if (optionalMatchSetDto.isPresent()) {
            set = optionalMatchSetDto.get();
        } else {
            throw new RuntimeException("Данный сет не сушествует");
        }

        Optional<GameDto> optionalGameDto = set.getGames().stream()
                .filter(gameDto -> gameDto.getIsOngoing() == true)
                .findFirst();

        if (optionalGameDto.isPresent()) {
            game = optionalGameDto.get();
        } else {
            throw new RuntimeException("Данный гейм не сушествует");
        }

        Optional<TieBreakDto> optionalTieBreakDto = set.getTieBreaks().stream()
                .filter(tieBreakDto -> tieBreakDto.getIsOngoing() == true)
                .findFirst();

        if (optionalTieBreakDto.isPresent()) {
            tieBreak = optionalTieBreakDto.get();
        } else {
            throw new RuntimeException("Данный тай-брейк не сушествует");
        }

        Optional<DeuceDto> optionalDeuceDto = game.getDeuces().stream()
                .filter(deuceDto -> deuceDto.getIsOngoing() == true)
                .findFirst();

        if (optionalDeuceDto.isPresent()) {
            deuce = optionalDeuceDto.get();
        } else {
            throw new RuntimeException("Данный деус не сушествует");
        }
    }

    private synchronized void checkDeuce(MatchStateDto match, SetDto set, GameDto game, DeuceDto deuce) {
        if (deuce.getPlayer1DeuceScore() >= 2 &&
                deuce.getPlayer1DeuceScore() - deuce.getPlayer2DeuceScore() >= 2) {

            deuce.setDeuceWinner(match.getPlayerOne());
            deuce.setIsOngoing(false);
            game.setGameWinner(match.getPlayerOne());
            game.setIsOngoing(false);
            set.setPlayer1GameScore(set.getPlayer1GameScore() + 1);
        }
        else if (deuce.getPlayer2DeuceScore() >= 2 &&
                deuce.getPlayer2DeuceScore() - deuce.getPlayer1DeuceScore() >= 2) {

            deuce.setDeuceWinner(match.getPlayerTwo());
            deuce.setIsOngoing(false);
            game.setGameWinner(match.getPlayerTwo());
            game.setIsOngoing(false);
            set.setPlayer2GameScore(set.getPlayer2GameScore() + 1);
        }
    }

    private synchronized void checkTieBreak(MatchStateDto match, SetDto set, TieBreakDto tieBreak) {
        if (tieBreak.getPlayer1TieBreakScore() >= 7 &&
                tieBreak.getPlayer1TieBreakScore() - tieBreak.getPlayer2TieBreakScore() >= 2) {

            tieBreak.setTieBreakWinner(match.getPlayerOne());
            tieBreak.setIsOngoing(false);
            set.setSetWinner(match.getPlayerOne());
            set.setIsOngoing(false);
            match.setPlayer1SetsScore(match.getPlayer1SetsScore() + 1);
        }
        else if (set.getPlayer2TieBreakScore() >= 7 &&
                set.getPlayer2TieBreakScore() - set.getPlayer1TieBreakScore() >= 2) {

            tieBreak.setTieBreakWinner(match.getPlayerTwo());
            tieBreak.setIsOngoing(false);
            set.setSetWinner(match.getPlayerTwo());
            set.setIsOngoing(false);
            match.setPlayer2SetsScore(match.getPlayer2SetsScore() + 1);
        }
    }

    private synchronized void checkSetWinner(MatchStateDto match, SetDto set) {
        if (set.getPlayer1GameScore() >= 6 &&
                set.getPlayer1GameScore() - set.getPlayer2GameScore() >= 2) {
            set.setSetWinner(match.getPlayerOne());
            set.setIsOngoing(false);
            finishSet(set);
        }
        else if (set.getPlayer2GameScore() >= 6 &&
                set.getPlayer2GameScore() - set.getPlayer1GameScore() >= 2) {
            set.setSetWinner(match.getPlayerTwo());
            set.setIsOngoing(false);
            finishSet(set);
        }
        else if (set.getPlayer1GameScore() == 6 &&
                set.getPlayer2GameScore() == 6) {
            match.setIsTieBreak(true);
        }
    }

    private synchronized void checkMatchFinished(MatchStateDto match) {
        int goal = match.getMatchTypeDto() == MatchTypeDto.SHORT_GAME ? 3 : 6;

        if (match.getPlayer1SetsScore() == goal){
            match.setMatchWinner(match.getPlayerOne());
            match.setIsFinished(true);
        }
        else if (match.getPlayer2SetsScore() == goal) {
            match.setMatchWinner(match.getPlayerTwo());
            match.setIsFinished(true);
        }
    }

    private synchronized void finishSet(SetDto set) {

    }

    private synchronized void resetScore(SetDto set) {
        set.setPlayer1CurrentScore(0);
        set.setPlayer2CurrentScore(0);
    }

    private synchronized void resetDeuce(MatchStateDto match, SetDto set) {
        set.setPlayer1DeuceScore(0);
        set.setPlayer2DeuceScore(0);
        match.setIsDeuce(false);
    }

    private synchronized void resetTieBreak(MatchStateDto match, SetDto set) {
        set.setPlayer1TieBreakScore(0);
        set.setPlayer2TieBreakScore(0);
        match.setIsTieBreak(false);
    }

    private synchronized void resetGame(SetDto set) {
        set.setPlayer1GameScore(0);
        set.setPlayer2GameScore(0);
    }


}
