package com.slava.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IMatchDAO<T, ID> {
    ID createMatch(T match);
    ID deleteMatch(ID uuidMatch);
    List<T> getAll();
    Optional<T> getById(ID uuid);
}
