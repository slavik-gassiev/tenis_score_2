package com.slava.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IOngoingMatchDAO<M, DTO, ID> {
     List<M> getAllMathes();
     Optional<DTO> getMatchByUUID(ID uuid);
     ID addMatch(M match);
     void deleteMath(ID uuid);
}
