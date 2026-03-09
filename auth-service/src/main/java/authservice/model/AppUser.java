package authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;

/**
 * Model for a user in the AUTH_USER.App_Users table.
 * Password hash is excluded from JSON responses via @JsonIgnore.
 */
public class AppUser {

    private long userId;
    private String username;

    @JsonIgnore // ← NEVER send the password hash back in API responses
    private String passwordHash;

    private String role;
    private Timestamp createdAt;

    // ── Getters & Setters ──

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
