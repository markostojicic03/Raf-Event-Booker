package rs.raf.backend.repository.tag;

import rs.raf.backend.model.TagModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySqlTagRepository implements TagRepository {
    private EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU")
            .createEntityManager();


    @Override
    public List<TagModel> findAll() {
        TypedQuery<TagModel> query = em.createQuery("SELECT t FROM TagModel t", TagModel.class);
        return query.getResultList();
    }

    @Override
    public TagModel findById(Long id) {
        return em.find(TagModel.class, id);
    }

    @Override
    public Set<TagModel> findAllByIds(Set<Long> ids) {
        if (ids.isEmpty()) return Set.of();
        TypedQuery<TagModel> q = em.createQuery(
                "SELECT t FROM TagModel t WHERE t.id IN :ids", TagModel.class);
        q.setParameter("ids", ids);
        return new HashSet<>(q.getResultList());
    }

    @Override
    public void save(TagModel tag) {
        em.getTransaction().begin();
        if (tag.getId() == null) {
            em.persist(tag); // Kreira novi zapis
        } else {
            em.merge(tag); // Ažurira postojeći
        }
        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        em.getTransaction().begin();
        TagModel category = em.find(TagModel.class, id);
        if (category != null) {
            em.remove(category);
        }
        em.getTransaction().commit();
    }
}
