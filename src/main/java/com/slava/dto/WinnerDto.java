package com.slava.dto;

import lombok.Data;

import java.util.List;

@Data
public class WinnerDto {
    private String matchWinnerName;
    private String player1Name;
    private String player2Name;
    private int player1SetsScore;
    private int player2SetsScore;

    private List<SetDto> sets; // Список сетов
}
