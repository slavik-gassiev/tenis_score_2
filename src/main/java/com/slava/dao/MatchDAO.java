package com.slava.dao;

import com.slava.dao.interfaces.IMatchDAO;
import com.slava.entity.Match;

import java.util.List;
import java.util.Optional;

public class MatchDAO implements IMatchDAO<Match, String> {
    @Override
    public String createMatch(Match match) {
        return null;
    }

    @Override
    public String deleteMatch(String uuidMatch) {
        return null;
    }

    @Override
    public List<Match> getAll() {
        return null;
    }

    @Override
    public Optional<Match> getById(String uuid) {
        return Optional.empty();
    }
}
