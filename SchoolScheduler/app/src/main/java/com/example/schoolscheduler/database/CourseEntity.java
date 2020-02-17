package com.example.schoolscheduler.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courses")
public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int termId;
    private int status;
    private String name;
    private Date start;
    private Date end;
    private String mentor;
    private String phone;
    private String email;

    @Ignore
    public CourseEntity() {
    }

    public CourseEntity(int id, int termId, int status,String name,
                        Date start, Date end, String mentor,
                        String phone, String email) {
        this.id = id;
        this.termId = termId;
        this.status=status;
        this.name = name;
        this.start = start;
        this.end = end;
        this.mentor = mentor;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public CourseEntity(int termId, int status,String name, Date start,
                        Date end, String mentor, String phone,
                        String email) {
        this.termId = termId;
        this.status = status;
        this.name = name;
        this.start = start;
        this.end = end;
        this.mentor = mentor;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", termId=" + termId +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", mentor='" + mentor + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
