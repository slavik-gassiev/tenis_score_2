package com.slava.dto;

import lombok.Data;

@Data
public class TableDto {
    private String player1;
    private String player2;
    private int player1Score;
    private int player2Score;
    private int player1Deuce;
    private int player2Deuce;
    private int player1Game;
    private int player2Game;
    private int player1TB;
    private int player2TB;
    private int player1Set;
    private int player2Set;
}
