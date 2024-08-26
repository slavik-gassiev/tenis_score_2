package com.slava.dao;

import com.slava.dao.interfaces.IOngoingMatchDAO;
import com.slava.dto.MatchStateDto;
import com.slava.entity.Match;

import java.util.List;
import java.util.Optional;

public class OngoingMatchDAO implements IOngoingMatchDAO<Match, MatchStateDto, String> {
    @Override
    public List<Match> getAllMathes() {
        return null;
    }

    @Override
    public Optional<MatchStateDto> getMatchByUUID(String uuid) {
        return Optional.empty();
    }

    @Override
    public String addMatch(Match match) {
        return null;
    }

    @Override
    public void deleteMath(String uuid) {

    }
}
