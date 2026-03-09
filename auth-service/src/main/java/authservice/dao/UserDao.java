package authservice.dao;

import authservice.model.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbc;

    public UserDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── Shared RowMapper (same pattern as StudentDao/CourseDao) ──
    private AppUser mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        AppUser u = new AppUser();
        u.setUserId(rs.getLong("User_Id"));
        u.setUsername(rs.getString("Username"));
        u.setPasswordHash(rs.getString("Password_Hash"));
        u.setRole(rs.getString("Role"));
        u.setCreatedAt(rs.getTimestamp("Created_At"));
        return u;
    }

    // ── Find a user by username (for login & duplicate checks) ──
    public AppUser findByUsername(String username) {
        String sql = "SELECT User_Id, Username, Password_Hash, Role, Created_At " +
                "FROM App_Users WHERE Username = ?";
        List<AppUser> r = jdbc.query(sql, this::mapRow, username);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Insert a new user (accepts AppUser object) ──
    public void create(AppUser user) {
        String sql = "INSERT INTO App_Users (Username, Password_Hash, Role) VALUES (?, ?, ?)";
        jdbc.update(sql, user.getUsername(), user.getPasswordHash(), user.getRole());
    }
}
