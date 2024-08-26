package com.slava.dao.interfaces;

public interface IPlayerDAO<T , ID >{
    ID createPlayer(T player);
}
