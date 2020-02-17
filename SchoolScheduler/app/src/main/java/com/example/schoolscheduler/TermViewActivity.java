package com.example.schoolscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.AppDatabase;
import com.example.schoolscheduler.database.AppRepository;
import com.example.schoolscheduler.database.CourseDao;
import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.CourseAdapter;
import com.example.schoolscheduler.ui.TermAdapter;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.viewmodel.CourseEditorViewModel;
import com.example.schoolscheduler.viewmodel.EditorViewModel;
import com.example.schoolscheduler.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.schoolscheduler.utilities.Constants.*;
import static com.example.schoolscheduler.utilities.Constants.TERM_ID_KEY;

public class TermViewActivity extends AppCompatActivity {

    @BindView(R.id.name_input) TextView mTextView;
    @BindView(R.id.start_input) TextView mTextView2;
    @BindView(R.id.end_input) TextView mTextView3;
    @BindView(R.id.button_course) ImageButton mCourse;
    @BindView(R.id.view_course) TextView mCourseText;

    private List<CourseEntity> courseData = new ArrayList<>();
    private List<CourseEntity> courseDataNow = new ArrayList<>();
    private List<CourseEntity> courseDataCurr = new ArrayList<>();
    private EditorViewModel mViewModel;
    private CourseEditorViewModel cViewModel;
    private MainViewModel oModel;
    private CourseAdapter mAdapter;
    private boolean mNewTerm, mEditing;
    private boolean canDel = false;
    private SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        mCourse.setVisibility(View.GONE);
        mCourseText.setVisibility(View.GONE);

        cViewModel= ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    public static String shortTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String catDate;
        catDate = Integer.toString(cal.get(Calendar.MONTH)+1) + "/" +
                Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" +
                Integer.toString(cal.get(Calendar.YEAR));
        return catDate;
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
            .get(EditorViewModel.class);
        mViewModel.mLiveTerm.observe(this, termEntity -> {
            if (termEntity != null && !mEditing) {
                currentTerm=termEntity.getId();
                cTerm=termEntity;
                mTextView.setText(termEntity.getTitle());
                mTextView2.setText(shortTime(termEntity.getStartDate()));
                mTextView3.setText(shortTime(termEntity.getEndDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_term));
            mNewTerm = true;
        } else {
            setTitle(getString(R.string.edit_term));
            int termId = extras.getInt(TERM_ID_KEY);
            mViewModel.loadData(termId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewTerm) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
            mCourse.setVisibility(View.VISIBLE);
            mCourseText.setVisibility(View.VISIBLE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            currentTerm=0;
            cTerm=null;
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            //get term id
            mViewModel = ViewModelProviders.of(this)
                    .get(EditorViewModel.class);
            mViewModel.mLiveTerm.observe(this, termEntity -> {
                if (termEntity != null && !mEditing) {
                    currentTerm=termEntity.getId();
                }
            });
            //Create delete validation
            final Observer<List<CourseEntity>> courseObserver =
                    new Observer<List<CourseEntity>>() {
                        @Override
                        public void onChanged(List<CourseEntity> courseEntities) {
                            courseData.clear();
                            courseData.addAll(courseEntities);
                            courseDataNow=courseData;
                            int add=0;
                            for(CourseEntity course : courseDataNow){
                                if (course.getTermId()==currentTerm) {
                                    courseDataCurr.add(course);
                                    if (course.getStatus() == 1 || course.getStatus() == 2) {
                                        add++;
                                    }
                                }
                            }
                            if(add==courseDataCurr.size()){
                                canDel=true;
                                mViewModel.deleteTerm();
                                for(CourseEntity course : courseDataNow){
                                    if (course.getTermId()==currentTerm) {
                                        cViewModel.deleteCourseNow(course);
                                    }
                                }
                            }
                            if(!canDel){
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Can't delete term with assigned courses", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    };
            oModel = ViewModelProviders.of(this)
                    .get(MainViewModel.class);
            oModel.mCourses.observe(this, courseObserver);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        currentTerm=0;
        cTerm=null;
        saveAndReturn();

    }

    private void saveAndReturn() {
        Constants.currentTerm=0;
        Date dStart = new Date();
        Date dEnd = new Date();
        try {
            dStart = formatter.parse(mTextView2.getText().toString());
            dEnd = formatter.parse(mTextView3.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mViewModel.saveTerm(dStart,dEnd,mTextView.getText().toString());
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    public void viewCourses(View view){
        Intent intent = new Intent(this, CourseViewer.class);
        startActivity(intent);
    }
}
