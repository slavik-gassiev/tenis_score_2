package com.slava.util;

import com.slava.dto.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Optional;

public class MapperUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Настройка правил маппинга
        PropertyMap<MatchDto, TableDto> matchToTableMap = new PropertyMap<MatchDto, TableDto>() {
            protected void configure() {
                // Маппинг имен игроков
                map().setPlayer1(source.getPlayerOne().getName());
                map().setPlayer2(source.getPlayerTwo().getName());

                // Маппинг сетов (используем текущий сет)
                if (source.getSets() != null && !source.getSets().isEmpty()) {
                    SetDto ongoingSet = source.getSets().stream()
                            .filter(SetDto::getIsOngoing)
                            .findFirst()
                            .orElse(SetDto.builder()
                                    .isOngoing(true)
                                    .player1GameScore(0)
                                    .player2GameScore(0)
                                    .tieBreak(TieBreakDto.builder()
                                            .isOngoing(false)
                                            .player1TieBreakScore(0)
                                            .player2TieBreakScore(0)
                                            .build())
                                    .build());

                    if (ongoingSet != null) {
                        // Маппинг текущей игры (используем игру с isOngoing = true)
                        map().setPlayer1Game(ongoingSet.getPlayer1GameScore());
                        map().setPlayer2Game(ongoingSet.getPlayer2GameScore());

                        GameDto ongoingGame = ongoingSet.getGames().stream()
                                .filter(GameDto::getIsOngoing)
                                .findFirst()
                                .orElse(GameDto.builder()
                                        .isOngoing(true)
                                        .player1CurrentScore(0)
                                        .player2CurrentScore(0)
                                        .deuce(DeuceDto.builder()
                                                .isOngoing(false)
                                                .player1DeuceScore(0)
                                                .player2DeuceScore(0)
                                                .build())
                                        .build());

                        if (ongoingGame != null) {
                            map().setPlayer1Score(ongoingGame.getPlayer1CurrentScore());
                            map().setPlayer2Score(ongoingGame.getPlayer2CurrentScore());

                            // Маппинг деюсов
                            if (ongoingGame.getDeuce() != null) {
                                map().setPlayer1Deuce(ongoingGame.getDeuce().getPlayer1DeuceScore());
                                map().setPlayer2Deuce(ongoingGame.getDeuce().getPlayer2DeuceScore());
                            }
                        }

                        // Маппинг счетов по тай-брейку
                        if (ongoingSet.getTieBreak() != null && ongoingSet.getTieBreak().getIsOngoing()) {
                            map().setPlayer1TB(ongoingSet.getTieBreak().getPlayer1TieBreakScore());
                            map().setPlayer2TB(ongoingSet.getTieBreak().getPlayer2TieBreakScore());
                        }
                    }
                }

                // Маппинг общего счета по сетам
                map().setPlayer1Set(source.getPlayer1SetsScore());
                map().setPlayer2Set(source.getPlayer2SetsScore());
            }
        };

        modelMapper.addMappings(matchToTableMap);
    }

    public static TableDto mapToTableDto(MatchDto matchDto) {
        Optional<TableDto> tableDto = Optional.of(modelMapper.map(matchDto, TableDto.class));
        if (tableDto.isPresent()) {
            return tableDto.get();
        }
        else  {
            throw new RuntimeException("Не удалось перевести MatchDto в TableDto");
        }
    }
}
