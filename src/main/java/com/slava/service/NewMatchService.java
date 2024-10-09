package com.slava.service;

import com.slava.dao.MatchDAO;
import com.slava.dao.PlayerDAO;
import com.slava.dao.interfaces.IMatchDAO;
import com.slava.dao.interfaces.IPlayerDAO;
import com.slava.dto.*;
import com.slava.entity.*;
import com.slava.service.interfaces.INewMatchService;
import com.slava.util.HibernateUtil;
import jakarta.servlet.annotation.WebServlet;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class NewMatchService implements INewMatchService<MatchDto, String, MatchTypeDto> {
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private IMatchDAO matchDAO = new MatchDAO();
    private IPlayerDAO playerDAO = new PlayerDAO();

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

        Player playerOne = (Player) playerDAO.findPlayerByName(winnerDto.getPlayer1Name())
                .orElseGet(() -> Player.builder()
                        .name(winnerDto.getPlayer1Name())
                        .build());

        Player playerTwo = (Player) playerDAO.findPlayerByName(winnerDto.getPlayer2Name())
                .orElseGet(() -> Player.builder()
                        .name(winnerDto.getPlayer2Name())
                        .build());

// Создаем список сетов, используя playerOne и playerTwo вместо Optional
        List<Set> sets = winnerDto.getSets().stream()
                .map(setDto -> Set.builder()
                        .setWinner(setDto.getSetWinner().getName().equals(playerOne.getName()) ? playerOne : playerTwo)
                        .player1GameScore(setDto.getPlayer1GameScore())
                        .player2GameScore(setDto.getPlayer2GameScore())
                        .tieBreak(TieBreak.builder()
                                .tieBreakWinner(setDto.getTieBreak().getTieBreakWinner() != null &&
                                        setDto.getTieBreak().getTieBreakWinner().getName().equals(playerOne.getName())
                                        ? playerOne : playerTwo)
                                .player1TieBreakScore(setDto.getTieBreak().getPlayer1TieBreakScore())
                                .player2TieBreakScore(setDto.getTieBreak().getPlayer2TieBreakScore())
                                .build())
                        .games(setDto.getGames().stream()
                                .map(gameDto -> Game.builder()
                                        .gameWinner(gameDto.getGameWinner() != null &&
                                                gameDto.getGameWinner().getName().equals(playerOne.getName())
                                                ? playerOne : playerTwo)
                                        .player1CurrentScore(gameDto.getPlayer1CurrentScore())
                                        .player2CurrentScore(gameDto.getPlayer2CurrentScore())
                                        .deuce(Deuce.builder()
                                                .deuceWinner(gameDto.getDeuce().getDeuceWinner() != null &&
                                                        gameDto.getDeuce().getDeuceWinner().getName().equals(playerOne.getName())
                                                        ? playerOne : playerTwo)
                                                .player1DeuceScore(gameDto.getDeuce().getPlayer1DeuceScore())
                                                .player2DeuceScore(gameDto.getDeuce().getPlayer2DeuceScore())
                                                .build())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

// Создаем объект Match, используя playerOne и playerTwo
        Match match = Match.builder()
                .winner(winnerDto.getMatchWinnerName().equals(playerOne.getName()) ? playerOne : playerTwo)
                .player1(playerOne)
                .player2(playerTwo)
                .sets(sets)
                .build();




        try {
            Optional<Long> matchId = matchDAO.saveMatch(match);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить матч в базу Hibernate" + e);
        }
    }
}
