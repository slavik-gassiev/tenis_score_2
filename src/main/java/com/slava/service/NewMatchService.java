package com.slava.service;

import com.slava.dto.MatchDto;
import com.slava.dto.MatchStateDto;
import com.slava.dto.MatchTypeDto;
import com.slava.dto.PlayerDto;
import com.slava.service.interfaces.INewMatchService;

import java.util.Optional;

public class NewMatchService implements INewMatchService<MatchDto, String, MatchTypeDto> {
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
}
