package com.slava.dao;

import com.slava.dao.interfaces.IPlayerDAO;
import com.slava.entity.Player;
import com.slava.util.HibernateUtil;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlayerDAO implements IPlayerDAO<Player, Optional<Long>, String> {


    @Override
    public Optional<Long> savePlayer(Player player) {
        Transaction transaction = null;
        Long id = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(player);
             id = player.getId();
            transaction.commit();

            return Optional.ofNullable(id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> findPlayerByName(String playerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM Player WHERE name = :name", Player.class);
            query.setParameter("name", playerName);
            List<Player> players = query.getResultList();
            return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
