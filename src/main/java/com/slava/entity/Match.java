package com.slava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerOne")
    private Player player1;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerTwo")
    private Player player2;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Winner")
    private Player winner;

    private int player1SetsScore;
    private int player2SetsScore;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Set> sets;

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
}