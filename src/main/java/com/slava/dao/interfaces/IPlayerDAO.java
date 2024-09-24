package com.slava.dao.interfaces;

import com.slava.entity.Player;

import java.util.Optional;

public interface IPlayerDAO<T , ID, S >{
    ID savePlayer(T player);
    Optional<T> findPlayerByName(S playerName);
}
