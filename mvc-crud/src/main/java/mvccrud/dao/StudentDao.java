package mvccrud.dao;

import mvccrud.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao {

    private final JdbcTemplate jdbc;

    public StudentDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── Shared RowMapper ──
    private Student mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Student s = new Student();
        s.setStudent_Id(rs.getLong("Student_Id"));
        s.setFull_Name(rs.getString("Full_Name"));
        s.setEmail(rs.getString("Email"));
        s.setPhone(rs.getString("Phone"));
        s.setDate_Of_Birth(rs.getDate("Date_Of_Birth"));
        s.setMajor(rs.getString("Major"));
        s.setStatus(rs.getString("Status"));
        s.setCreated_At(rs.getTimestamp("Created_At"));
        return s;
    }

    // ── List all students (newest first) ──
    public List<Student> findAll() {
        String sql = "SELECT Student_Id, Full_Name, Email, Phone, Date_Of_Birth, " +
                "Major, Status, Created_At FROM Students ORDER BY Student_Id DESC";
        return jdbc.query(sql, this::mapRow);
    }

    // ── Find one student by ID ──
    public Student findById(long studentId) {
        String sql = "SELECT Student_Id, Full_Name, Email, Phone, Date_Of_Birth, " +
                "Major, Status, Created_At FROM Students WHERE Student_Id = ?";
        List<Student> r = jdbc.query(sql, this::mapRow, studentId);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Find a student by email ──
    public Student findByEmail(String email) {
        String sql = "SELECT Student_Id, Full_Name, Email, Phone, Date_Of_Birth, " +
                "Major, Status, Created_At FROM Students WHERE Email = ?";
        List<Student> r = jdbc.query(sql, this::mapRow, email);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Insert a new student (accepts Student object) ──
    public void create(Student student) {
        String sql = "INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                student.getFull_Name(),
                student.getEmail(),
                student.getPhone(),
                student.getDate_Of_Birth(),
                student.getMajor(),
                student.getStatus() != null && !student.getStatus().isEmpty()
                        ? student.getStatus()
                        : "Active");
    }

    // ── Update a student (accepts Student object, returns rows affected) ──
    public int update(Student student) {
        String sql = "UPDATE Students SET Full_Name = ?, Email = ?, Phone = ?, " +
                "Date_Of_Birth = ?, Major = ?, Status = ? WHERE Student_Id = ?";
        return jdbc.update(sql,
                student.getFull_Name(),
                student.getEmail(),
                student.getPhone(),
                student.getDate_Of_Birth(),
                student.getMajor(),
                student.getStatus(),
                student.getStudent_Id());
    }

    // ── Delete a student (returns rows affected) ──
    public int delete(long studentId) {
        String sql = "DELETE FROM Students WHERE Student_Id = ?";
        return jdbc.update(sql, studentId);
    }
}
