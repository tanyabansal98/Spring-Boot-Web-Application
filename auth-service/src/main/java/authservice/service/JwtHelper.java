package authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Helper class for JWT (JSON Web Token) operations.
 *
 * A JWT looks like this: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSJ9.xyz
 * ↑ header ↑ payload (claims) ↑ signature
 *
 * - generateToken(): creates a JWT with the username + role baked inside
 * - parseToken(): reads the JWT back, verifies the signature, returns the
 * claims
 */
@Component
public class JwtHelper {

    private final SecretKey key;
    private final long expirationMs;

    public JwtHelper(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // ── Generate a JWT token ──
    // Input: username = "alice", role = "USER"
    // Output: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSIsInJvbGUiOiJVU0VSIn0.abc123"
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username) // "sub" claim = who this token belongs to
                .claim("role", role) // custom claim = user's role
                .issuedAt(now) // "iat" claim = when token was created
                .expiration(expiry) // "exp" claim = when token expires
                .signWith(key) // sign with our secret key (HMAC-SHA256)
                .compact(); // build the final string
    }

    // ── Parse and validate a JWT token ──
    // Input: "eyJhbGciOiJIUzI1NiJ9..."
    // Output: Claims object containing {sub: "alice", role: "USER", iat: ..., exp:
    // ...}
    // Throws: JwtException if token is invalid, expired, or tampered with
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key) // verify signature matches our key
                .build()
                .parseSignedClaims(token) // parse & validate the token
                .getPayload(); // return the claims (payload)
    }
}
