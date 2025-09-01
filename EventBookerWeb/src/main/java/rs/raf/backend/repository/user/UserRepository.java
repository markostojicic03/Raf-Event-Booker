package rs.raf.backend.repository.user;

import rs.raf.backend.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UserModel> findAll();
    UserModel findById(Long id);
    void save(UserModel user);
    void delete(Long id);
    public Optional<UserModel> findUserByEmail(String email);

}
