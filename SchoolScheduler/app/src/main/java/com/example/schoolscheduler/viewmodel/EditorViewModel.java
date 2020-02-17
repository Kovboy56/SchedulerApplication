package com.example.schoolscheduler.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.TermAdapter;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<TermEntity> mLiveTerm =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TermEntity term = mRepository.getTermById(termId);
                mLiveTerm.postValue(term);
            }
        });
    }

    public void saveTerm(Date start, Date end, String termText) {
        TermEntity term = mLiveTerm.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(termText.trim())) {
                return;
            }
            term = new TermEntity(start, end,termText.trim());
        } else {
            term.setStartDate(start);
            term.setEndDate(end);
            term.setTitle(termText.trim());
        }
        mRepository.insertTerm(term);
    }

    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerm.getValue());
    }
}
