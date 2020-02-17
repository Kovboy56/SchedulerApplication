package com.example.schoolscheduler.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.TermAdapter;
import com.example.schoolscheduler.utilities.Constants;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestEditorViewModel extends AndroidViewModel {

    public MutableLiveData<TestEntity> mLiveTest =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TestEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int testId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TestEntity test = mRepository.getTestById(testId);
                mLiveTest.postValue(test);
            }
        });
    }

    public void saveTest(int courseId, Date start ,String code, String name, String description) {
        TestEntity test = mLiveTest.getValue();

        if (test == null) {
            if (TextUtils.isEmpty(name.trim())) {
                return;
            }
            test = new TestEntity(Constants.currentCourse,start,code,name.trim(),description);
        } else {
            test.setCourseId(courseId);
            test.setStartDate(start);
            test.setCode(code);
            test.setName(name);
            test.setDescription(description);
        }
        mRepository.insertTest(test);
    }

    public void deleteTest() {
        mRepository.deleteTest(mLiveTest.getValue());
    }
}
