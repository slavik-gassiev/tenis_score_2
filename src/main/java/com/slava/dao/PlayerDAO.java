package com.slava.dao;

import com.slava.dao.interfaces.IPlayerDAO;
import com.slava.entity.Player;
import com.slava.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.OptionalLong;

public class PlayerDAO implements IPlayerDAO<Player, Optional<Long>> {


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
}
