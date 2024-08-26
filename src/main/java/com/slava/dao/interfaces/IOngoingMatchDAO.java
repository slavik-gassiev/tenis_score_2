package com.slava.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IOngoingMatchDAO<M, DTO, ID> {
     Optional<List<M>> getAllMatches();
     Optional<DTO> getMatchByUUID(ID uuid);
     Optional<ID> addMatch(M match);
     void deleteMath(ID uuid);
}
