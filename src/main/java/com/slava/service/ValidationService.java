package com.slava.service;

public class ValidationService {
    public void checkParameterForNewGame(String player1, String player2, String matchType) {
        if (player1.isBlank() || player2.isEmpty() || matchType.isBlank()) {
            throw new RuntimeException("вы не ввели данные");
        }
        if (player1 == player2) {
            throw new RuntimeException("Пожалуйста введи разных игроков");
        }
    }
}
