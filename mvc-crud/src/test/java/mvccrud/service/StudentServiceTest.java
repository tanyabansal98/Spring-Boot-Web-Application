package mvccrud.service;

import mvccrud.dao.StudentDao;
import mvccrud.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private final StudentDao dao = Mockito.mock(StudentDao.class);
    private final StudentService service = new StudentService(dao);

    @Test
    @DisplayName("createStudent() persists a new Student and returns it with status Active")
    void createStudent() {
        // The student object we want to create
        Student input = new Student(
                0L,
                "Bob",
                "bob@example.com",
                null,
                null,
                "Math",
                null,
                null);

        // Persisted entity that DAO returns after the INSERT
        Student persisted = new Student(
                1L,
                "Bob",
                "bob@example.com",
                null,
                null,
                "Math",
                "Active",
                new Timestamp(System.currentTimeMillis()));

        // 1st call to findByEmail -> null (no duplicate found, allow insert)
        // 2nd call to findByEmail -> persisted (fetch the saved record)
        when(dao.findByEmail("bob@example.com")).thenReturn(null, persisted);

        // create() is void, let it do nothing
        doNothing().when(dao).create(input);

        // WHEN
        Student result = service.createStudent(input);

        // THEN
        assertThat(result.getStudent_Id()).isEqualTo(1L);
        assertThat(result.getFull_Name()).isEqualTo("Bob");
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
        assertThat(result.getStatus()).isEqualTo("Active");

        verify(dao, times(1)).create(input);
        verify(dao, times(2)).findByEmail("bob@example.com");
    }
}
