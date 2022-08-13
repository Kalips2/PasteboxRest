package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteEnum.Expiration;
import com.example.pasteboxrest.PastModel.PasteboxEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public PasteBox getByHash(String hash) throws BoxNotExist, IncorrectHash {
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

        if(isAlive(instance)) {
            return new PasteBox(instance.getTitle(), instance.getBody());
        } else {
            deleteByInstance(instance);
            throw new BoxNotExist("Pastebox's time is passed and it doesn't exist");
        }
    }

    @Override
    public List<PasteBox> getAllPublicPaste() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<PasteboxEntity> instance = em.createQuery("select a from PasteboxEntity a " +
                "where a.exposure = 'PUBLIC'" +
                "order by a.id", PasteboxEntity.class).setMaxResults(10).getResultList();
        em.getTransaction().commit();
        em.close();

        List<PasteBox> result = instance.stream()
                .filter(e -> isAlive(e))
                .map(e-> new PasteBox(e.getTitle(), e.getBody()))
                .collect(Collectors.toList());

        instance.stream()
                .filter(e -> !isAlive(e))
                .forEach(this::deleteByInstance);

        return result;
    }

    @Override
    public int getMaxId() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Integer max = em.createQuery("SELECT MAX(id) FROM PasteboxEntity ", Integer.class).getSingleResult();
        em.getTransaction().commit();
        em.close();

        if (max == null) max = 0;
        return max;
    }

    @Override
    public boolean isAlive(PasteboxEntity instance) {
        Expiration expiration = instance.getExpiration();
        Timestamp timestamp = instance.getTimeCreated();
        Long different = getTimeByEnum(expiration);
        Long dif = System.currentTimeMillis() - timestamp.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(dif);
        if (seconds <= different) return true;
        else return false;
    }

    @Override
    public void deleteByInstance(PasteboxEntity instance) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PasteboxEntity attached = em.merge(instance);
        em.remove(attached);
        em.getTransaction().commit();
        em.close();
    }

    public Long getTimeByEnum(Expiration expiration){
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
                return 0L;
            }
        }
    }
}
