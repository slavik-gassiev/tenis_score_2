package com.slava.service;

import java.rmi.RemoteException;

public class ValidationService {
    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    public void checkParameterForNewGame(String player1, String player2, String matchType) {
        if (player1.isBlank() || player2.isBlank() || matchType.isBlank()) {
            throw new RuntimeException("вы не ввели данные");
        }
        if (player1 == player2) {
            throw new RuntimeException("Пожалуйста введи разных игроков");
        }
    }

    public void isUuidCorrect(String uuid) throws RemoteException {
        if(uuid.isBlank()) {
            throw new RemoteException("Uuid не верный!");
        }
        if (!(ongoingMatchService.isMatchExist(uuid))) {
            throw new RuntimeException("Не можем найти матч по uuid");
        }
    }
}
