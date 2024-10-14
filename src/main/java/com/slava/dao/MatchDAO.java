package com.slava.dao;

import com.slava.dao.interfaces.IMatchDAO;
import com.slava.entity.Match;
import com.slava.entity.Player;
import com.slava.util.HibernateUtil;
import jakarta.persistence.Query;
import org.h2.util.SmallMap;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class MatchDAO implements IMatchDAO<Match, Long> {

    @Override
    public Optional<Long> saveMatch(Match match) {
        Transaction transaction = null;
        Long id = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(match);
            id = match.getId();
            transaction.commit();
            return Optional.of(id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();  // Закрываем сессию
            }
        }
    }

    @Override
    public Optional<Long> deleteMatch(Long id) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete from Match where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            return Optional.of(id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();  // Закрываем сессию
            }
        }
    }

    @Override
    public List<Match> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            List<Match> from_match = session.createQuery("from Match", Match.class).getResultList();
            return from_match;
        } finally {
            if (session != null) {
                session.close();  // Закрываем сессию
            }
        }
    }

    @Override
    public Optional<Match> getById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return Optional.ofNullable(session.get(Match.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();  // Закрываем сессию
            }
        }
    }

    public Player findOrCreatePlayer(String playerName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Player WHERE name = :name", Player.class);
        query.setParameter("name", playerName);
        List<Player> players = query.getResultList();

        if (!players.isEmpty()) {
            return players.get(0); // Возвращаем существующего игрока
        } else {
            Player newPlayer = new Player();
            newPlayer.setName(playerName);
            session.beginTransaction();
            session.save(newPlayer);
            session.getTransaction().commit();
            return newPlayer; // Возвращаем нового игрока
        }
    }
}
