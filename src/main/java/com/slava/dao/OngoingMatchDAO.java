package com.slava.dao;

import com.slava.dao.interfaces.IOngoingMatchDAO;
import com.slava.dto.MatchStateDto;
import com.slava.entity.Match;
import com.slava.service.OngoingMatchService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class OngoingMatchDAO implements IOngoingMatchDAO<MatchStateDto, String> {

    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    @Override
    public List<MatchStateDto> getAllMatches() {
        return  ongoingMatchService.getMatches().values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MatchStateDto> getMatchByUUID(String uuid) {
        Optional<MatchStateDto> match = Optional.ofNullable(ongoingMatchService.getMatches().get(uuid));
         if (match.isPresent()){
            return match;
        } else {
             return Optional.empty();
         }
    }

    @Override
    public Optional<String> addMatch(MatchStateDto match) {
        String uuid = UUID.randomUUID().toString();
        ongoingMatchService.getMatches().put(uuid, match);
        return Optional.ofNullable(uuid);
    }

    @Override
    public void deleteMath(String uuid) {
        ongoingMatchService.getMatches().remove(uuid);
    }
}
