package rs.raf.backend.repository.category;

import rs.raf.backend.model.CategoryModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.util.List;

public class MySqlCategoryRepository implements CategoryRepository {
    private EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU")
            .createEntityManager();


    @Override
    public List<CategoryModel> findAll() {
        TypedQuery<CategoryModel> query = em.createQuery("SELECT c FROM CategoryModel c", CategoryModel.class);
        return query.getResultList();
    }

    @Override
    public CategoryModel findById(Long id) {
        return em.find(CategoryModel.class, id);
    }

    @Override
    public List<CategoryModel> findAllCategoriesById(Long categoryId) {
        TypedQuery<CategoryModel> query = em.createQuery("SELECT e FROM EventModel e WHERE e.category.id = :categoryId", CategoryModel.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public CategoryModel searchByName(String name) {
        TypedQuery<CategoryModel> query = em.createQuery(
                "SELECT c FROM CategoryModel c WHERE LOWER(c.categoryName) = LOWER(:name)", CategoryModel.class);
        query.setParameter("name", name);
        List<CategoryModel> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void save(CategoryModel category) {
        em.getTransaction().begin();
        if (category.getId() == null) {
            em.persist(category); // ovde mogu da kreiram novi zapis
        } else {
            em.merge(category); // a ovde ako treba da azuriram postojeci
        }
        em.getTransaction().commit();
    }

    @Override
    public boolean delete(Long id) {
        long eventCount = em.createQuery(
                        "SELECT COUNT(e) FROM EventModel e WHERE e.category.id = :catId", Long.class)
                .setParameter("catId", id)
                .getSingleResult();

        if (eventCount > 0) {
            return false;
        }

        CategoryModel c = em.find(CategoryModel.class, id);
        if (c == null) return false;

        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
        return true;
    }

    @Override
    public int countByNameIgnoreCase(String name) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM CategoryModel c WHERE LOWER(c.categoryName) = LOWER(:name)",
                        Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return count.intValue();
    }
}
