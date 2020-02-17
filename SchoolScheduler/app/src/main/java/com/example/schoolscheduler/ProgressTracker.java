package com.example.schoolscheduler;

import android.os.Bundle;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.viewmodel.EditorViewModel;
import com.example.schoolscheduler.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.schoolscheduler.utilities.Constants.currentTerm;

public class ProgressTracker extends AppCompatActivity {

    private List<CourseEntity> courseData = new ArrayList<>();
    private List<CourseEntity> courseDataNow = new ArrayList<>();
    private List<CourseEntity> courseDataCurr = new ArrayList<>();
    private EditorViewModel mViewModel;
    private MainViewModel oModel;

    @BindView(R.id.course_complete)
    TextView comCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        //Create delete validation
        final Observer<List<CourseEntity>> courseObserver =
                new Observer<List<CourseEntity>>() {
                    @Override
                    public void onChanged(List<CourseEntity> courseEntities) {
                        courseData.clear();
                        courseData.addAll(courseEntities);
                        courseDataNow=courseData;
                        int add=0;
                        int com=0;
                        for(CourseEntity course : courseDataNow){
                            add++;
                            if(course.getStatus()==1){
                                com++;
                            }
                        }
                        comCourse.setText(com +"/"+ add);
                    }
                };
        oModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        oModel.mCourses.observe(this, courseObserver);

    }

}
