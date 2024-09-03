package com.slava.service;

import com.slava.dao.OngoingMatchDAO;
import com.slava.dto.MatchDto;
import com.slava.dto.PlayerDto;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
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


    public synchronized Boolean isMatchExist(String matchId) {
        if (matchDAO.getMatchByUUID(matchId) != null){
            return true;
        }
        return  false;
    }

    public Boolean isPlayerInMatch(String matchId, PlayerDto player) {
        Optional<MatchDto> matchDto = matchDAO.getMatchByUUID(matchId);

        if (!(matchDto.isPresent())) {
            throw new RuntimeException("Данный матч не сушествует");
        }
        if (matchDto.get().getPlayerOne().getName() == player.getName()) return true;
        if (matchDto.get().getPlayerTwo().getName() == player.getName()) return true;

        return false;
    }
}
