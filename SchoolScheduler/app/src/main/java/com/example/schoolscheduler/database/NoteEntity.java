package com.example.schoolscheduler.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int courseId;
    private String text;
    private Date startDate;


    @Ignore
    public NoteEntity() {
    }

    @Ignore
    public NoteEntity(int courseId, String text, Date startDate) {
        this.courseId = courseId;
        this.text = text;
        this.startDate = startDate;
    }

    public NoteEntity(int id, int courseId, String text, Date startDate) {
        this.id = id;
        this.courseId = courseId;
        this.text = text;
        this.startDate = startDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", text=" + text +
                ", startDate=" + startDate +
                '}';
    }
}
