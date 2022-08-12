package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.Exceptions.NotFindExpInSwitchException;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteEnum.Expiration;
import com.example.pasteboxrest.PastModel.PasteboxEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class PastRepositoryImpl implements PastRepository {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");

    @Override
    public String addPaste(PasteBoxRequest pasteBoxRequest) {

        int hash = getMaxId() + 1;
        Timestamp time = new Timestamp(System.currentTimeMillis());
        PasteboxEntity ent = new PasteboxEntity(
                pasteBoxRequest.getTitle(),
                pasteBoxRequest.getBody(),
                Integer.toHexString(hash),
                pasteBoxRequest.getExposure(),
                pasteBoxRequest.getExpiration(),
                time);
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(ent);
        em.getTransaction().commit();
        em.close();

        return Integer.toHexString(hash);
    }

    @Override
    public PasteBox getByHash(String hash) throws NotFindExpInSwitchException, BoxNotExist, IncorrectHash {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PasteboxEntity instance;
        try {
            instance = em.createQuery("select a from PasteboxEntity a where a.hash=:hash", PasteboxEntity.class)
                    .setParameter("hash", hash)
                    .getSingleResult();
        } catch (Exception e) {
            throw new IncorrectHash("IncorrectHash");
        } finally {
            em.getTransaction().commit();
            em.close();
        }

        if(isAlive(instance.getExpiration(), instance.getTimeCreated())) {
            return new PasteBox(instance.getTitle(), instance.getBody());
        } else {
            deleteByHash(instance);
            throw new BoxNotExist("Pastebox's time is passed and it doesn't exist");
        }
    }

    @Override
    public List<PasteBox> getTenPublicPast() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Object[]> instance = em.createQuery("select a.title, a.body from PasteboxEntity a " +
                "where a.exposure = 'PUBLIC'" +
                "order by a.id", Object[].class).setMaxResults(10).getResultList();
        em.getTransaction().commit();
        em.close();

        List<PasteBox> result = new ArrayList<>();
        for (Object[] row : instance) {
            PasteBox container = new PasteBox();
            container.setTitle((String) row[0]);
            container.setBody((String) row[1]);
            result.add(container);
        }
        return result;
    }

    @Override
    public int getMaxId() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Integer max = em.createQuery("SELECT MAX(id) FROM PasteboxEntity ", Integer.class).getSingleResult();
        em.getTransaction().commit();
        em.close();

        if (max == null) max = 1;
        return max;
    }

    @Override
    public boolean isAlive(Expiration expiration, Timestamp timestamp) throws NotFindExpInSwitchException {
        Long different = getTimeByEnum(expiration);
        Long dif = System.currentTimeMillis() - timestamp.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(dif);
        if (seconds <= different) return true;
        else return false;
    }

    @Override
    public void deleteByHash(PasteboxEntity instance) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PasteboxEntity attached = em.merge(instance);
        em.remove(attached);
        em.getTransaction().commit();
        em.close();
    }

    public Long getTimeByEnum(Expiration expiration) throws NotFindExpInSwitchException {
        switch (expiration) {
            case HOUR_1 -> {
                return 60 * 60L;
            }
            case MIN_10 -> {
                return 10 * 60L;
            }
            case HOUR_3 -> {
                return 3 * 60 * 60L;
            }
            case WEEK_1 -> {
                return 1 * 7 * 24 * 60 * 60L;
            }
            case MONTH_1 -> {
                return 4 * 7 * 24 * 60 * 60L;
            }
            default -> {
                throw new NotFindExpInSwitchException("Error!");
            }
        }
    }
}
