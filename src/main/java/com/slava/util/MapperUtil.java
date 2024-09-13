package com.slava.util;

import com.slava.dto.*;
import com.slava.service.NewMatchService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;


public class MapperUtil {
    private static final ModelMapper modelMapper = new ModelMapper();


    static {
        // Настройка правил маппинга для преобразования MatchDto в TableDto
        PropertyMap<MatchDto, TableDto> matchToTableMap = new PropertyMap<MatchDto, TableDto>() {
            protected void configure() {
                // Маппинг имен игроков
                map().setPlayer1(source.getPlayerOne().getName());
                map().setPlayer2(source.getPlayerTwo().getName());



                // Маппинг текущей игры (используем игру с isOngoing = true)
                map().setPlayer1Game(source.getOngoingSet().getPlayer1GameScore());
                map().setPlayer2Game(source.getOngoingSet().getPlayer2GameScore());


                map().setPlayer1Score(source.getOngoingSet().getOngoingGame().getPlayer1CurrentScore());
                map().setPlayer2Score(source.getOngoingSet().getOngoingGame().getPlayer2CurrentScore());

                // Маппинг деюсов
                    map().setPlayer1Deuce(source.getOngoingSet().getOngoingGame().getDeuce().getPlayer1DeuceScore());
                    map().setPlayer2Deuce(source.getOngoingSet().getOngoingGame().getDeuce().getPlayer2DeuceScore());


                // Маппинг счетов по тай-брейку
                    map().setPlayer1TB(source.getOngoingSet().getTieBreak().getPlayer1TieBreakScore());
                    map().setPlayer2TB(source.getOngoingSet().getTieBreak().getPlayer2TieBreakScore());


                // Маппинг общего счета по сетам
                map().setPlayer1Set(source.getPlayer1SetsScore());
                map().setPlayer2Set(source.getPlayer2SetsScore());
            }
        };

        modelMapper.addMappings(matchToTableMap);

        // Настройка правил маппинга для преобразования MatchDto в WinnerDto
        PropertyMap<MatchDto, WinnerDto> matchToWinnerMap = new PropertyMap<MatchDto, WinnerDto>() {
            protected void configure() {
                // Маппинг имен игроков и победителя
                map().setMatchWinnerName(source.getMatchWinner().getName());
                map().setPlayer1Name(source.getPlayerOne().getName());
                map().setPlayer2Name(source.getPlayerTwo().getName());

                // Маппинг счета по сетам
                map().setPlayer1SetsScore(source.getPlayer1SetsScore());
                map().setPlayer2SetsScore(source.getPlayer2SetsScore());

                // Маппинг списка сетов
                map().setSets(source.getSets());
            }
        };

        modelMapper.addMappings(matchToWinnerMap);
    }

    // Преобразование MatchDto в TableDto
    public static TableDto mapToTableDto(MatchDto matchDto) {
        NewMatchService newMatchService = new NewMatchService();
        if (matchDto == null) {
            throw new IllegalArgumentException("MatchDto не может быть null");
        }

        List<GameDto> games = matchDto.getOngoingSet().getGames();
        if (games.stream().filter(GameDto::getIsOngoing).count() == 0) {
            games.add(newMatchService.initGame(newMatchService.initDeuce()));
            matchDto.getOngoingSet().setGames(games);
        }

        List<SetDto> sets = matchDto.getSets();
        if (sets.stream().filter(SetDto::getIsOngoing).count() == 0) {
            sets.add(newMatchService.initSet());
            matchDto.setSets(sets);
        }

        return modelMapper.map(matchDto, TableDto.class);
    }

    // Преобразование MatchDto в WinnerDto
    public static WinnerDto mapToWinnerDto(MatchDto matchDto) {
        if (matchDto == null) {
            throw new IllegalArgumentException("MatchDto не может быть null");
        }
        return modelMapper.map(matchDto, WinnerDto.class);
    }
}

