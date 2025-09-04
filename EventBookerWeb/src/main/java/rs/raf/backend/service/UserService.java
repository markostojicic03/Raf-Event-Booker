package rs.raf.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.digest.DigestUtils;
import rs.raf.backend.model.UserModel;
import rs.raf.backend.repository.user.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private static final String SECRET = "myVerySecretKeyThatIs256BitsLong12345678";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUser(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(UserModel u) {
        ensureUniqueEmail(u, null);
        if (u.getPassword() == null || !u.getPassword().equals(u.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        u.setPasswordHash(DigestUtils.sha256Hex(u.getPassword()));
        userRepository.save(u);
    }

    public void updateUser(UserModel u) {
        UserModel existing = userRepository.findById(u.getId());
        if (existing == null) return;
        ensureUniqueEmail(u, u.getId());

        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            if (!u.getPassword().equals(u.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            existing.setPasswordHash(DigestUtils.sha256Hex(u.getPassword()));
        }

        existing.setFirstName(u.getFirstName());
        existing.setLastName(u.getLastName());
        existing.setEmail(u.getEmail());
        existing.setRole(u.getRole());

        userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    //// login

    public String login(String email, String password) {
        String hashedPassword = DigestUtils.sha256Hex(password);


        System.out.println("SIFRAA: "+hashedPassword);
        return userRepository.findUserByEmail(email)
                .filter(u -> u.getPasswordHash().equals(hashedPassword))
                .filter(UserModel::isActive)
                .map(u -> JWT.create()
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .withSubject(u.getEmail())
                        .withClaim("role", u.getRole())
                        .sign(ALGORITHM))
                .orElse(null);
    }

    public boolean isAuthorized(String token) {
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM).build().verify(token);
            return userRepository.findUserByEmail(jwt.getSubject()).isPresent();
        } catch (Exception ex) {
            return false;
        }
    }
    /// kraj logina
    private void ensureUniqueEmail(UserModel user, Long excludeId) {
        Optional<UserModel> existing = userRepository.findUserByEmail(user.getEmail());
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("E-mail već postoji!");
        }
    }


}
