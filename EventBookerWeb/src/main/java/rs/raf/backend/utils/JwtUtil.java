package rs.raf.backend.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public final class JwtUtil {
    private static final String SECRET = "myVerySecretKeyThatIs256BitsLong12345678"; // ≥ 256 bits
    private static final Key KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static String createToken(String email, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60)) // 1 h
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }
}