package com.example.schoolscheduler;

import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.NoteDao;
import com.example.schoolscheduler.database.NoteEntity;
import com.example.schoolscheduler.ui.NoteAdapter;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.viewmodel.MainViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.schoolscheduler.utilities.Constants.COURSE_ID_KEY;
import static com.example.schoolscheduler.utilities.Constants.cTerm;
import static com.example.schoolscheduler.utilities.Constants.currentTerm;

public class NoteViewer extends AppCompatActivity {

    @BindView(R.id.recycler_view_note)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab_note)
    void fabClickHandler() {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        startActivity(intent);
    }

    private List<NoteEntity> noteData = new ArrayList<>();
    private NoteAdapter mAdapter;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {

        final Observer<List<NoteEntity>> noteObserver =
                new Observer<List<NoteEntity>>() {
                    @Override
                    public void onChanged(List<NoteEntity> noteEntities) {
                        noteData.clear();
                        //noteData.addAll(noteEntities);
                        for(NoteEntity note: noteEntities){
                            if(note.getCourseId()== Constants.currentCourse){
                                noteData.add(note);
                            }
                        }

                        if(mAdapter == null){
                            mAdapter=new NoteAdapter(noteData,
                                    NoteViewer.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mViewModel.mNotes.observe(this, noteObserver);

        Bundle extras = getIntent().getExtras();
        setTitle("Notes for " + Constants.cCourse.getName());
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }
}
