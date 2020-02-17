package com.example.schoolscheduler.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.schoolscheduler.TermViewActivity;

import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "terms")
public class TermEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date startDate;
    private Date endDate;
    private String title;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Ignore
    public TermEntity() {
    }

    @Ignore
    public TermEntity(int id, Date startDate, String title) {
        this.id = id;
        this.startDate = startDate;
        this.title = title;
    }

    public TermEntity(int id, Date startDate, Date endDate, String title) {
        this.id = id;
        this.startDate = startDate;
        this.title = title;
        this.endDate = endDate;
    }

    @Ignore
    public TermEntity(Date startDate, Date endDate,String title) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStringDate() {

        return TermViewActivity.shortTime(startDate) +
                " - " + TermViewActivity.shortTime(endDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TermEntity{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                '}';
    }
}
