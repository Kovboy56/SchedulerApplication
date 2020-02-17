package com.example.schoolscheduler.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.TermAdapter;
import com.example.schoolscheduler.utilities.Constants;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseEditorViewModel extends AndroidViewModel {

    public MutableLiveData<CourseEntity> mLiveCourse =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int courseId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CourseEntity course = mRepository.getCourseById(courseId);
                mLiveCourse.postValue(course);
            }
        });
    }

    public void saveCourse(int termId,int status,String name, Date start, Date end, String mentor, String phone, String email) {
        CourseEntity course = mLiveCourse.getValue();

        if (course == null) {
            if (TextUtils.isEmpty(name.trim())) {
                return;
            }
            course = new CourseEntity(Constants.currentTerm,status,name.trim(),start, end, mentor,phone,email);
        } else {
            course.setTermId(termId);
            course.setStatus(status);
            course.setName(name);
            course.setStart(start);
            course.setEnd(end);
            course.setMentor(mentor);
            course.setPhone(phone);
            course.setEmail(email);
        }
        mRepository.insertCourse(course);
    }

    public void deleteCourse() {
        mRepository.deleteCourse(mLiveCourse.getValue());
    }

    public void deleteCourseNow(CourseEntity course) {
        mRepository.deleteCourse(course);
    }
}
