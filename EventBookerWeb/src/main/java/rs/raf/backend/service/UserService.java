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

    public void createUser(UserModel user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
    //// login

    public String login(String email, String password) {
        String hashedPassword = DigestUtils.sha256Hex(password);

        System.out.println("SIFRAA: "+hashedPassword);
        return userRepository.findUserByEmail(email)
                .filter(u -> u.getPasswordHash().equals(hashedPassword))
                .map(u -> JWT.create()
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .withSubject(u.getEmail())
                        .withClaim("role", u.getRole())
                        .sign(ALGORITHM))
                .orElse(hashedPassword);
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



}
