package com.example.schoolscheduler.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.schoolscheduler.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<CourseEntity>> mCourses;
    public LiveData<List<TermEntity>> mTerms;
    public LiveData<List<NoteEntity>> mNotes;
    public LiveData<List<TestEntity>> mTests;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTerms = getAllTerms();
        mCourses = getAllCourses();
        mNotes = getAllNotes();
        mTests = getAllTests();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insertAll(SampleData.getTerms());
                mDb.courseDao().insertAll(SampleData.getCourses());
                mDb.noteDao().insertAll(SampleData.getNotes());
                mDb.testDao().insertAll(SampleData.getTests());
            }
        });
    }

    private LiveData<List<TermEntity>> getAllTerms() {
        return mDb.termDao().getAll();
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return mDb.courseDao().getAll();
    }

    private LiveData<List<NoteEntity>> getAllNotes() {
        return mDb.noteDao().getAll();
    }

    private LiveData<List<TestEntity>> getAllTests() {
        return mDb.testDao().getAll();
    }

    public void deleteAllTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteAll();
                mDb.courseDao().deleteAll();
                mDb.noteDao().deleteAll();
                mDb.testDao().deleteAll();
            }
        });
    }

    public TermEntity getTermById(int termId) {
        return mDb.termDao().getTermById(termId);
    }

    public CourseEntity getCourseById(int courseId) {
        return mDb.courseDao().getCourseById(courseId);
    }

    public NoteEntity getNoteById(int noteId) {
        return mDb.noteDao().getNoteById(noteId);
    }

    public TestEntity getTestById(int testId) {
        return mDb.testDao().getTestById(testId);
    }

    public void insertTerm(TermEntity term) {

        executor.execute(new Runnable() {
            @Override
            public void run() { mDb.termDao().insertTerm(term); }
        });

    }

    public void insertCourse(CourseEntity course) {

        executor.execute(new Runnable() {
            @Override
            public void run() { mDb.courseDao().insertCourse(course); }
        });

    }

    public void insertNote(NoteEntity note) {

        executor.execute(new Runnable() {
            @Override
            public void run() { mDb.noteDao().insertNote(note); }
        });

    }

    public void insertTest(TestEntity test) {

        executor.execute(new Runnable() {
            @Override
            public void run() { mDb.testDao().insertTest(test); }
        });

    }

    public void deleteTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteTerm(term);
            }
        });
    }

    public void deleteCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().deleteCourse(course);
            }
        });
    }

    public void deleteNote(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().deleteNote(note);
            }
        });
    }

    public void deleteTest(final TestEntity test) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.testDao().deleteTest(test);
            }
        });
    }
}
