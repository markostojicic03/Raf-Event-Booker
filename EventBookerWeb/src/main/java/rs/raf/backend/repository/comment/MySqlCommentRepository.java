package rs.raf.backend.repository.comment;

import rs.raf.backend.model.CommentModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MySqlCommentRepository implements CommentRepository {
    private EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU")
            .createEntityManager();

    @Override
    public List<CommentModel> findByEventIdOrderByCreatedAtDesc(Long eventId) {
        return em.createQuery(
                        "select c from CommentModel c where c.event.id = :id order by c.createdAt desc",
                        CommentModel.class)
                .setParameter("id", eventId)
                .getResultList();
    }

    @Override
    public void save(CommentModel comment) {
        em.getTransaction().begin();

        if (comment.getId() == null) {
            em.persist(comment);
        } else {
            em.merge(comment);
        }
        em.getTransaction().commit();
    }
}
