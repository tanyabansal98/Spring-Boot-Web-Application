package mvccrud.dao;

import mvccrud.model.Enrollment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentDao {

    private final JdbcTemplate jdbc;

    public EnrollmentDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── RowMapper with JOIN data for display ──
    private Enrollment mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Enrollment e = new Enrollment();
        e.setEnrollmentId(rs.getLong("Enrollment_Id"));
        e.setStudentId(rs.getLong("Student_Id"));
        e.setCourseId(rs.getLong("Course_Id"));
        e.setGrade(rs.getString("Grade"));
        e.setEnrolledAt(rs.getTimestamp("Enrolled_At"));
        try {
            e.setStudentName(rs.getString("Full_Name"));
        } catch (Exception ex) {
        }
        try {
            e.setCourseName(rs.getString("Course_Name"));
        } catch (Exception ex) {
        }
        try {
            e.setCourseCode(rs.getString("Course_Code"));
        } catch (Exception ex) {
        }
        return e;
    }

    // ── Get all courses a student is enrolled in ──
    public List<Enrollment> findByStudentId(long studentId) {
        String sql = "SELECT e.Enrollment_Id, e.Student_Id, e.Course_Id, e.Grade, e.Enrolled_At, " +
                "c.Course_Name, c.Course_Code " +
                "FROM Enrollments e " +
                "JOIN Courses c ON e.Course_Id = c.Course_Id " +
                "WHERE e.Student_Id = ? ORDER BY e.Enrolled_At DESC";
        return jdbc.query(sql, this::mapRow, studentId);
    }

    // ── Get all students enrolled in a course ──
    public List<Enrollment> findByCourseId(long courseId) {
        String sql = "SELECT e.Enrollment_Id, e.Student_Id, e.Course_Id, e.Grade, e.Enrolled_At, " +
                "s.Full_Name, c.Course_Name, c.Course_Code " +
                "FROM Enrollments e " +
                "JOIN Students s ON e.Student_Id = s.Student_Id " +
                "JOIN Courses  c ON e.Course_Id  = c.Course_Id " +
                "WHERE e.Course_Id = ? ORDER BY s.Full_Name ASC";
        return jdbc.query(sql, this::mapRow, courseId);
    }

    // ── Enroll a student in a course ──
    public void enroll(long studentId, long courseId) {
        String sql = "INSERT INTO Enrollments (Student_Id, Course_Id) VALUES (?, ?)";
        jdbc.update(sql, studentId, courseId);
    }

    // ── Drop a student from a course (returns rows affected) ──
    public int drop(long studentId, long courseId) {
        String sql = "DELETE FROM Enrollments WHERE Student_Id = ? AND Course_Id = ?";
        return jdbc.update(sql, studentId, courseId);
    }

    // ── Update a grade ──
    public int updateGrade(long studentId, long courseId, String grade) {
        String sql = "UPDATE Enrollments SET Grade = ? WHERE Student_Id = ? AND Course_Id = ?";
        return jdbc.update(sql, grade, studentId, courseId);
    }

    // ── Check if already enrolled ──
    public boolean isEnrolled(long studentId, long courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Student_Id = ? AND Course_Id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, studentId, courseId);
        return count != null && count > 0;
    }
}
