package rs.raf.backend.service;

import rs.raf.backend.model.UserModel;
import rs.raf.backend.repository.user.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    // Ubaciš konkretan repo (za sad ručno)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUser(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(UserModel user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
