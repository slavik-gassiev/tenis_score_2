package com.slava.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "PlayerOne")
    private Player player1;
    @ManyToOne
    @JoinColumn(name = "PlayerTwo")
    private Player player2;
    @ManyToOne
    @JoinColumn(name = "Winner")
    private Player winner;

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
}