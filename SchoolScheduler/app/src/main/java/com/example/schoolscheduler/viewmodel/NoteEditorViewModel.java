package com.example.schoolscheduler.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.NoteEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.TermAdapter;
import com.example.schoolscheduler.utilities.Constants;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteEditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> mLiveNote =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NoteEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = mRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(int courseId, String text,Date start) {
        NoteEntity note = mLiveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(text.trim())) {
                return;
            }
            note = new NoteEntity(Constants.currentCourse,text.trim(),start);
        } else {
            note.setCourseId(courseId);
            note.setText(text);
            note.setStartDate(start);
        }
        mRepository.insertNote(note);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
