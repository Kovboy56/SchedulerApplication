package com.example.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.NoteEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.utilities.SampleData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<TermEntity>> mTerms;
    public LiveData<List<CourseEntity>> mCourses;
    public LiveData<List<NoteEntity>> mNotes;
    public LiveData<List<TestEntity>> mTests;
    private AppRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
        mCourses  = mRepository.mCourses;
        mNotes  = mRepository.mNotes;
        mTests  = mRepository.mTests;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public LiveData<List<CourseEntity>> getAll() {
        return mRepository.getAllCourses();
    };

    public void deleteAllTerms() {
        mRepository.deleteAllTerms();
    }
}
