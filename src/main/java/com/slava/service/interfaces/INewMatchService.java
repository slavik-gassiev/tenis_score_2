package com.slava.service.interfaces;

import java.util.Optional;

public interface INewMatchService <M, P, T>{
    Optional<M> initMatch(P player1, P player2, T matchType);
}
