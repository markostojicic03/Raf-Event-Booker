package rs.raf.backend.repository.user;

import rs.raf.backend.model.UserModel;

import java.util.List;

public interface UserRepository {
    List<UserModel> findAll();
    UserModel findById(Long id);
    void save(UserModel user);
    void delete(Long id);
}
