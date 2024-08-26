package com.slava.dao.interfaces;

public interface IPlayerDAO<T , ID >{
    ID savePlayer(T player);
}
