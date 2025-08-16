package rs.raf.backend.repository.event;

import rs.raf.backend.model.EventModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class MySqlEventRepository implements EventRepository {
    private EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU")
            .createEntityManager();

    @Override
    public List<EventModel> findAll() {
        return em.createQuery("SELECT e FROM EventModel e", EventModel.class)
                .getResultList();
    }

    @Override
    public List<EventModel> findAllByCategoryId(Long categoryId) {
        return em.createQuery(
                        "SELECT e FROM EventModel e WHERE e.category.id = :categoryId",
                        EventModel.class)
                .setParameter("categoryId", categoryId)   
                .getResultList();
    }

    @Override
    public EventModel findById(Long id) {
        return em.find(EventModel.class, id);
    }

    @Override
    public void save(EventModel event) {
        em.getTransaction().begin();
        if (event.getId() == null) {
            em.persist(event);
        } else {
            em.merge(event);
        }
        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        EventModel event = findById(id);
        if (event != null) {
            em.getTransaction().begin();
            em.remove(event);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<EventModel> findLatest(int limit) {
        return em.createQuery(
                        "SELECT e FROM EventModel e ORDER BY e.createdAt DESC",
                        EventModel.class
                )
                .setMaxResults(limit)
                .getResultList();
    }
}
