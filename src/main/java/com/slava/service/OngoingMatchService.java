package com.slava.service;

import com.slava.dto.MatchStateDto;
import com.slava.dto.PlayerDto;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OngoingMatchService {
    private static OngoingMatchService INSTANCE;
    private OngoingMatchService() {};

    private volatile Map<String, MatchStateDto> matches = new HashMap<>();

    public synchronized static OngoingMatchService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OngoingMatchService();
        }
        return INSTANCE;
    }


    public synchronized Boolean isMatchExist(String matchId) {
        if (matches.containsKey(matchId)){
            return true;
        }
        return  false;
    }

    public Boolean isPlayerInMatch(String matchId, PlayerDto player) {
        String nameOne = matches.get(matchId).getPlayerOne().getName();
        String nameTwo = matches.get(matchId).getPlayerTwo().getName();

        if (nameOne == player.getName() || nameTwo == player.getName()) {
            return true;
        }
        return false;
    }
}
