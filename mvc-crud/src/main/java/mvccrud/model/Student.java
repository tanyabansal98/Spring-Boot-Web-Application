package mvccrud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

// Serializable is REQUIRED for Redis to store this object in cache
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("studentId")
    private long Student_Id;

    @JsonProperty("fullName")
    private String Full_Name;

    @JsonProperty("email")
    private String Email;

    @JsonProperty("phone")
    private String Phone;

    @JsonProperty("dob")
    private Date Date_Of_Birth;

    @JsonProperty("major")
    private String Major;

    @JsonProperty("status")
    private String Status;

    @JsonProperty("createdAt")
    private Timestamp Created_At;

    // ── Constructors ──
    public Student() {
    }

    public Student(long Student_Id, String Full_Name, String Email,
            String Phone, Date Date_Of_Birth, String Major,
            String Status, Timestamp Created_At) {
        this.Student_Id = Student_Id;
        this.Full_Name = Full_Name;
        this.Email = Email;
        this.Phone = Phone;
        this.Date_Of_Birth = Date_Of_Birth;
        this.Major = Major;
        this.Status = Status;
        this.Created_At = Created_At;
    }

    // ── Getters & Setters ──
    public long getStudent_Id() {
        return Student_Id;
    }

    public void setStudent_Id(long id) {
        this.Student_Id = id;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String name) {
        this.Full_Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public Date getDate_Of_Birth() {
        return Date_Of_Birth;
    }

    public void setDate_Of_Birth(Date dob) {
        this.Date_Of_Birth = dob;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        this.Major = major;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public Timestamp getCreated_At() {
        return Created_At;
    }

    public void setCreated_At(Timestamp ts) {
        this.Created_At = ts;
    }
}
