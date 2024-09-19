package com.slava.service;

import com.slava.dao.MatchDAO;
import com.slava.dao.interfaces.IMatchDAO;
import com.slava.dto.*;
import com.slava.entity.Match;
import com.slava.entity.Player;
import com.slava.service.interfaces.INewMatchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewMatchService implements INewMatchService<MatchDto, String, MatchTypeDto> {

    IMatchDAO matchDAO = new MatchDAO();
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();

    public MatchDto initMatch(String player1, String player2, MatchTypeDto matchType) {
        if (ongoingMatchService.isPlayerInMatches(player1)) {
            throw new RuntimeException("Игрок " +player1 +" уже играет в другом матче!");
        }
        if (ongoingMatchService.isPlayerInMatches(player2)) {
            throw new RuntimeException("Игрок " +player2 +" уже играет в другом матче!");
        }

        SetDto setDto = initSet();

        PlayerDto playerDto1 = initPlayer(player1);

        PlayerDto playerDto2 = initPlayer(player2);

        return initNewMatch(matchType,playerDto1, playerDto2);

    }

    public SetDto initSet() {
        TieBreakDto tieBreakDto = initTieBreak();

        DeuceDto deuceDto = initDeuce();

        GameDto gameDto = initGame(deuceDto);

        List<GameDto> gameDtoList = new ArrayList<>();
        gameDtoList.add(gameDto);

        SetDto setDto = SetDto.builder()
                .isOngoing(true)
                .player1GameScore(0)
                .player2GameScore(0)
                .games(gameDtoList)
                .tieBreak(tieBreakDto)
                .build();
        return setDto;
    }

    public MatchDto initNewMatch(MatchTypeDto matchType, PlayerDto playerDto1, PlayerDto playerDto2) {
        List<SetDto> setDtoList = new ArrayList<>();
        setDtoList.add(initSet());
        return MatchDto.builder()
                .playerOne(playerDto1)
                .playerTwo(playerDto2)
                .matchType(matchType)
                .matchState(MatchStateDto.ONGOING)
                .sets(setDtoList)
                .build();
    }

    public GameDto initGame(DeuceDto deuceDto) {
        GameDto gameDto = GameDto.builder()
                .isOngoing(true)
                .player1CurrentScore(0)
                .player2CurrentScore(0)
                .deuce(deuceDto)
                .build();
        return gameDto;
    }

    private PlayerDto initPlayer(String player) {
        PlayerDto playerDto = PlayerDto.builder()
                .name(player)
                .build();
        return playerDto;
    }

    public DeuceDto initDeuce() {
        DeuceDto deuceDto = DeuceDto.builder()
                .player1DeuceScore(0)
                .player2DeuceScore(0)
                .build();
        return deuceDto;
    }

    public TieBreakDto initTieBreak() {
        TieBreakDto tieBreakDto = TieBreakDto.builder()
                .player1TieBreakScore(0)
                .player2TieBreakScore(0)
                .build();
        return tieBreakDto;
    }

    public void addMatchToDB(WinnerDto winnerDto) {
        Player winner = Player.builder()
                .name(winnerDto.getMatchWinnerName())
                .build();
        Player playerOne = Player.builder()
                .name(winnerDto.getPlayer1Name())
                .build();
        Player playerTwo = Player.builder()
                .name(winnerDto.getPlayer2Name())
                .build();

        Match match = Match.builder()
                .winner(winner)
                .player1(playerOne)
                .player2(playerTwo)
                .build();

        try {
            matchDAO.saveMatch(match);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить матч в базу Hibernate" + e);
        }
    }
}
