package com.slava.dao;

import com.slava.dao.interfaces.IMatchDAO;
import com.slava.entity.Match;
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
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(match);
            id = match.getId();
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
    public Optional<Long> deleteMatch(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.createQuery("delete Match where Match.id = :id", Match.class)
                    .setParameter("id", id)
                    .executeUpdate();

            return Optional.of(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Match> getAll() {
            Session session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("select m from Match m", Match.class).getResultList();
    }

    @Override
    public Optional<Match> getById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            return Optional.ofNullable(session.get(Match.class, id));
        }catch (Exception e) {
            return Optional.empty();
        }
    }
}
