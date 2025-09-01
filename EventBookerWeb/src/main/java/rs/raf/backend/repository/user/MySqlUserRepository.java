package rs.raf.backend.repository.user;

import rs.raf.backend.model.UserModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class MySqlUserRepository implements UserRepository {
    private EntityManager em = Persistence
            .createEntityManagerFactory("eventBookerPU")
            .createEntityManager();

    @Override
    public List<UserModel> findAll() {
        return em.createQuery("SELECT u FROM UserModel u", UserModel.class)
                .getResultList();
    }

    @Override
    public UserModel findById(Long id) {
        return em.find(UserModel.class, id);
    }

    @Override
    public void save(UserModel user) {
        em.getTransaction().begin();
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        UserModel user = findById(id);
        if (user != null) {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        }
    }

    @Override
    public Optional<UserModel> findUserByEmail(String email) {
        return em.createQuery(
                        "SELECT u FROM UserModel u WHERE u.email = :email", UserModel.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
