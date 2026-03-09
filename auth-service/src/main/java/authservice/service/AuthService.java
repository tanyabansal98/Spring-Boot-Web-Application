package authservice.service;

import authservice.dao.UserDao;
import authservice.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Auth Service — handles registration, login, and token validation.
 *
 * Flow:
 * Register: username + password → hash password → save to DB
 * Login: username + password → check DB → verify hash → generate JWT
 * Validate: JWT token → parse & verify → return username + role
 */
@Service
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder; // BCrypt (from Spring Security)
    private final JwtHelper jwtHelper;

    public AuthService(UserDao userDao, PasswordEncoder passwordEncoder, JwtHelper jwtHelper) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    // ── Register a new user ──
    public AppUser register(String username, String password) {
        // 1. Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        // 2. Check for duplicate username
        username = username.trim().toLowerCase();
        AppUser existing = userDao.findByUsername(username);
        if (existing != null) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }

        // 3. Hash the password using BCrypt
        // "password123" → "$2a$10$N9qo8uLOickgx2ZMRZoMye..."
        // This hash is ONE-WAY — you cannot reverse it back to "password123"
        String hash = passwordEncoder.encode(password);

        // 4. Create the user object and save
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(hash);
        user.setRole("USER"); // default role

        userDao.create(user);

        // 5. Return the created user (password hash is excluded by @JsonIgnore)
        return userDao.findByUsername(username);
    }

    // ── Login and get a JWT token ──
    public Map<String, String> login(String username, String password) {
        // 1. Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // 2. Find the user
        username = username.trim().toLowerCase();
        AppUser user = userDao.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        // 3. Verify the password against the stored hash
        // BCrypt.matches("password123", "$2a$10$N9qo8u...") → true/false
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        // 4. Generate a JWT token
        String token = jwtHelper.generateToken(user.getUsername(), user.getRole());

        // 5. Return the token + user info
        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole());
    }

    // ── Validate a JWT token ──
    public Map<String, String> validateToken(String token) {
        try {
            Claims claims = jwtHelper.parseToken(token);
            return Map.of(
                    "username", claims.getSubject(),
                    "role", claims.get("role", String.class),
                    "valid", "true");
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
    }
}
