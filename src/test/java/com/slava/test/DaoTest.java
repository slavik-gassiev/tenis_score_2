package com.slava.test;
import com.slava.entity.Match;
import com.slava.entity.Player;
import com.slava.service.NewMatchService;
import com.slava.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class DaoTest {


    @Test
    void checkSavaMatch() {
        Transaction transaction = null;
        Long id = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Player winner = Player.builder()
                    .name("winner")
                    .build();
            Player playerOne = Player.builder()
                    .name("player1")
                    .build();
            Player playerTwo = Player.builder()
                    .name("player2")
                    .build();

            Match match = Match.builder()
                    .winner(winner)
                    .player1(playerOne)
                    .player2(playerTwo)
                    .build();

            session.persist(match);
            id = match.getId();
            Match match1 = session.get(Match.class, id);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }
}
