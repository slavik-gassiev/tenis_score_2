package com.slava.service;

import com.slava.dao.MatchDAO;
import com.slava.dao.interfaces.IMatchDAO;
import com.slava.dto.*;
import com.slava.entity.Match;
import com.slava.entity.Player;
import com.slava.service.interfaces.INewMatchService;

import java.util.Optional;

public class NewMatchService implements INewMatchService<MatchDto, String, MatchTypeDto> {

    IMatchDAO matchDAO = new MatchDAO();
    private OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();

    public Optional<MatchDto> initMatch(String player1, String player2, MatchTypeDto matchType) {
        if (ongoingMatchService.isPlayerInMatches(player1)) {
            throw new RuntimeException("Игрок " +player1 +" уже играет в другом матче!");
        }
        if (ongoingMatchService.isPlayerInMatches(player2)) {
            throw new RuntimeException("Игрок " +player2 +" уже играет в другом матче!");
        }

        PlayerDto playerDto1 = PlayerDto.builder()
                .name(player1)
                .build();

        PlayerDto playerDto2 = PlayerDto.builder()
                .name(player2)
                .build();

        return Optional.of(MatchDto.builder()
                .playerOne(playerDto1)
                .playerTwo(playerDto2)
                .matchType(matchType)
                .matchState(MatchStateDto.ONGOING)
                .build());

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
