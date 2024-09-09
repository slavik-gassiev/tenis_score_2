package com.slava.dao;

import com.slava.dao.interfaces.IOngoingMatchDAO;
import com.slava.dto.MatchDto;
import com.slava.service.OngoingMatchService;

import java.util.*;
import java.util.stream.Collectors;

public class OngoingMatchDAO implements IOngoingMatchDAO<MatchDto, String> {

    private static Map<String, MatchDto> matches = new HashMap<>();
    @Override
    public List<MatchDto> getAllMatches() {
        return  matches.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MatchDto> getMatchByUUID(String uuid) {
        Optional<MatchDto> match = Optional.ofNullable(matches.get(uuid));
         if (match.isPresent()){
            return match;
        } else {
             return Optional.empty();
         }
    }

    @Override
    public Optional<String> addMatch(MatchDto match) {
        String uuid = UUID.randomUUID().toString();
        matches.put(uuid, match);
        return Optional.ofNullable(uuid);
    }

    public void updateMatch(String uuid, MatchDto match) {
        matches.replace(uuid, match);
    }

    @Override
    public void deleteMath(String uuid) {
        matches.remove(uuid);
    }
}
