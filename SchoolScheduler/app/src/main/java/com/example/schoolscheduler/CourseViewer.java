package com.example.schoolscheduler;

import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.CourseDao;
import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.ui.CourseAdapter;
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

public class CourseViewer extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, CourseEditorActivity.class);
        startActivity(intent);
    }

    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseAdapter mAdapter;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Constants.currentCourse=0;
        Constants.cCourse=null;
        System.out.println("Nullified");

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {

        final Observer<List<CourseEntity>> courseObserver =
                new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                courseData.clear();
                //courseData.addAll(courseEntities);
                for(CourseEntity course: courseEntities){
                    if(course.getTermId()== Constants.currentTerm){
                        courseData.add(course);
                    }
                }

                if(mAdapter == null){
                    mAdapter=new CourseAdapter(courseData,
                            CourseViewer.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mViewModel.mCourses.observe(this, courseObserver);

        Bundle extras = getIntent().getExtras();
        setTitle("Courses for " + cTerm.getTitle());
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
