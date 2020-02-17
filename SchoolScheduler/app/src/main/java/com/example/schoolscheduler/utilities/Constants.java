package com.example.schoolscheduler.utilities;

import android.app.PendingIntent;

import androidx.lifecycle.LiveData;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.TermEntity;

import java.util.ArrayList;

public class Constants {
    public static final String TERM_ID_KEY = "term_id_key";
    public static final String EDITING_KEY = "editing_key";
    public static final String NOTE_ID_KEY = "note_id_key";
    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String TEST_ID_KEY = "test_id_key";
    public static int currentTerm;
    public static TermEntity cTerm;
    public static int currentCourse;
    public static CourseEntity cCourse;
    public static int sampID1 = 0;
    public static int sampID2 = 0;
    public static int sampID3 = 0;
    public static ArrayList<PendingIntent> intentArray = new ArrayList<>();
    public static String displayMessage = "Start";
}
