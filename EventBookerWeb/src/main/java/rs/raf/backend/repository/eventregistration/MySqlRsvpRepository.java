package rs.raf.backend.repository.eventregistration;

import rs.raf.backend.model.RsvpModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class MySqlRsvpRepository implements RsvpRepository {
    private final EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU").createEntityManager();

    @Override public List<RsvpModel> findByEventId(Long eventId){
        return em.createQuery("SELECT r FROM RsvpModel r WHERE r.eventId = :id", RsvpModel.class)
                .setParameter("id", eventId).getResultList();
    }
    @Override public Optional<RsvpModel> findByEventIdAndEmail(Long eventId, String email){
        try{
            return Optional.of(em.createQuery(
                            "SELECT r FROM RsvpModel r WHERE r.eventId = :id AND r.email = :em", RsvpModel.class)
                    .setParameter("id", eventId)
                    .setParameter("em", email)
                    .getSingleResult());
        }catch(NoResultException e){ return Optional.empty(); }
    }
    @Override public void save(RsvpModel r){
        em.getTransaction().begin();
        if (r.getId() == null) em.persist(r); else em.merge(r);
        em.getTransaction().commit();
    }
    @Override public int countByEventId(Long eventId){
        Long cnt = em.createQuery(
                        "SELECT COUNT(r) FROM RsvpModel r WHERE r.eventId = :id", Long.class)
                .setParameter("id", eventId)
                .getSingleResult();
        return cnt.intValue();
    }
}