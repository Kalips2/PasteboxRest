package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteboxEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public PasteBox getByHash(String hash) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PasteboxEntity instance = em.createQuery("select a from PasteboxEntity a where a.hash=:hash", PasteboxEntity.class)
                .setParameter("hash", hash)
                .getSingleResult();
        em.getTransaction().commit();
        em.close();
        return new PasteBox(instance.getTitle(), instance.getBody());
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
        int max = em.createQuery("SELECT MAX(id) FROM PasteboxEntity ", Integer.class).getSingleResult();
        em.getTransaction().commit();
        em.close();
        return max;
    }
}
