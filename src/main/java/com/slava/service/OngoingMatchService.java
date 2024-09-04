package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.MatchDto;
import com.slava.dto.PlayerDto;
import lombok.Data;

import java.util.Optional;

@Data
public class OngoingMatchService {
    private static OngoingMatchService INSTANCE;
//    обернуть dao  в сервис
    private OngoingMatchService() {};

    private OngoingMatchDAO matchDAO = new OngoingMatchDAO();

    public synchronized static OngoingMatchService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OngoingMatchService();
        }
        return INSTANCE;
    }


    public  Boolean isMatchExist(String matchId) {
        if (matchDAO.getMatchByUUID(matchId) != null){
            return true;
        }
        return  false;
    }

    public String addMatchToTrack(Optional<MatchDto> matchDto) {
        if(matchDto.isPresent()) {
           Optional<String> uuid =  matchDAO.addMatch(matchDto.get());
           return uuid.get();
        }
        else {
            throw new RuntimeException("не удалось сохранить матч в трек лист");
        }
    }

    public MatchDto getMatch(String uuid) {
        Optional<MatchDto> matchDto = matchDAO.getMatchByUUID(uuid);
        if(matchDto.isPresent()) {
            return matchDto.get();
        }
        else {
            throw new RuntimeException("Не удаеться получить матч из базы данных");
        }
    }

    public Boolean isPlayerInMatch(String matchId, PlayerDto player) {
        Optional<MatchDto> matchDto = matchDAO.getMatchByUUID(matchId);

        if (!(matchDto.isPresent())) {
            return false;
        }
        if (matchDto.get().getPlayerOne().getName() == player.getName()) return true;
        if (matchDto.get().getPlayerTwo().getName() == player.getName()) return true;

        return false;
    }

    public Boolean isPlayerInMatches(String player) {
        Optional<MatchDto> matchDto = matchDAO.getAllMatches().stream()
                .filter(matchDto1 -> matchDto1.getPlayerOne().getName() == player ||
                        matchDto1.getPlayerTwo().getName() == player)
                .findFirst();

        if (matchDto.isPresent()) return true;
        return false;
    }
}
