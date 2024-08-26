package com.slava.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IMatchDAO<T, ID> {
    Optional<ID> saveMatch(T match);
    Optional<ID> deleteMatch(ID id);
    Optional<List<T>> getAll();
    Optional<T> getById(ID id);
}
