package com.slava.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IOngoingMatchDAO<DTO, ID> {
     List<DTO> getAllMatches();
     Optional<DTO> getMatchByUUID(ID uuid);
     Optional<ID> addMatch(DTO match);
     void deleteMath(ID uuid);
}
