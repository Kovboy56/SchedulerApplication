package com.example.schoolscheduler.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "tests")
public class TestEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int courseId;
    private Date startDate;
    private String code;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", startDate=" + startDate +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Ignore
    public TestEntity(int courseId, Date startDate, String code, String name, String description) {
        this.courseId = courseId;
        this.startDate = startDate;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public TestEntity(int id, int courseId,  Date startDate, String code, String name, String description) {
        this.id = id;
        this.courseId = courseId;
        this.startDate = startDate;
        this.code = code;
        this.name = name;
        this.description = description;
    }


    @Ignore
    public TestEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
