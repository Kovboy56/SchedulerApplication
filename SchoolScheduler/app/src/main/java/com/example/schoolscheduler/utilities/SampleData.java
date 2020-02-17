package com.example.schoolscheduler.utilities;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.NoteEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.database.TestEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class SampleData {

    private static final String title1 = "Spring 2020";
    private static final String title2 = "Summer 2020";
    private static final String title3 = "Winter 2021";

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    private static Date getPD(String strThatDay){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date d = null;
        try {
            d = formatter.parse(strThatDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        return cal.getTime();
    }

    public static List<TermEntity> getTerms(){
        List<TermEntity> terms = new ArrayList<>();
        TermEntity samp1 = new TermEntity(501,getPD("01/01/2020"),getPD("07/01/2020"),title1);
        TermEntity samp2 = new TermEntity(502,getPD("07/01/2020"),getPD("12/01/2020"),title2);
        TermEntity samp3 = new TermEntity(503,getPD("01/01/2021"),getPD("07/01/2021"),title3);
        terms.add(samp1);
        terms.add(samp2);
        terms.add(samp3);
        return terms;
    }

    public static List<CourseEntity> getCourses() {
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity(500,501,0,"Math 101",getPD("01/01/2020"),getPD("07/01/2020"),"Tem Mohammed","4801231234","tem@wgu.edu"));
        courses.add(new CourseEntity(501,501,0,"Semantics 204",getPD("01/01/2020"),getPD("07/01/2020"),"Steven Chicci","4801231234","chicci@wgu.edu"));
        courses.add(new CourseEntity(502,502,0,"Programming 101",getPD("07/01/2020"),getPD("12/01/2020"),"Steven Tyler","4801231234","tyler@wgu.edu"));
        courses.add(new CourseEntity(503,502,0,"Statistics 457",getPD("07/01/2020"),getPD("12/01/2020"),"Matt Tano","4801231234","matto@wgu.edu"));
        courses.add(new CourseEntity(504,503,0,"Daredevil 469",getPD("01/01/2021"),getPD("07/01/2021"),"Steve O","4804206969","steveo@gmail.com"));
        courses.add(new CourseEntity(505,503,0,"Flying 234",getPD("01/01/2021"),getPD("07/01/2021"),"Marry Poppins","1234567890","spoonful@gmail.com"));
        return courses;
    }

    public static List<NoteEntity> getNotes() {
        List<NoteEntity> notes = new ArrayList<>();
        notes.add(new NoteEntity(500,500, "Note1", getDate(0)));
        notes.add(new NoteEntity(501,501, "Note2", getDate(-1)));
        notes.add(new NoteEntity(502,502, "Note3", getDate(-2)));
        return notes;
    }

    public static List<TestEntity> getTests() {
        List<TestEntity> tests = new ArrayList<>();
        tests.add(new TestEntity(500,500,getDate(0),"DFH75","Math 101 OA","Objective Assessment"));
        tests.add(new TestEntity(501,501,getDate(-1),"JK8LO","Semantics PA","Performance Assessment"));
        tests.add(new TestEntity(502,502,getDate(-2),"PA01F","Programming PA","Performance Assessment"));
        tests.add(new TestEntity(503,503,getDate(-3),"JKLOP","Statistics OA","Objective Assessment"));
        tests.add(new TestEntity(504,504,getDate(-4),"W1NKL","Daredevil PA","Performance Assessment"));
        tests.add(new TestEntity(505,505,getDate(-5),"F17KJ","Flying OA","Objective Assessment"));
        return tests;
    }
}
