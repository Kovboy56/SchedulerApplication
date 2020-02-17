package com.example.schoolscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.utilities.AlertReceiver;
import com.example.schoolscheduler.utilities.AlertReceiverEnd;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.viewmodel.CourseEditorViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.schoolscheduler.utilities.Constants.COURSE_ID_KEY;
import static com.example.schoolscheduler.utilities.Constants.EDITING_KEY;
import static com.example.schoolscheduler.utilities.Constants.cCourse;
import static com.example.schoolscheduler.utilities.Constants.currentCourse;

public class CourseEditorActivity extends AppCompatActivity {

    @BindView(R.id.name_input) TextView mName;
    @BindView(R.id.start_input) TextView mStart;
    @BindView(R.id.end_input) TextView mEnd;
    @BindView(R.id.mentor_input) TextView mMentor;
    @BindView(R.id.phone_input) TextView mPhone;
    @BindView(R.id.email_input) TextView mEmail;
    @BindView(R.id.view_notes) ImageButton mViewNote;
    @BindView(R.id.textView4) TextView mViewNoteText;
    @BindView(R.id.view_tests) TextView mViewAssText;
    @BindView(R.id.view_assess) ImageButton mViewAssess;
    @BindView(R.id.r_progress) RadioButton rProgress;
    @BindView(R.id.r_completed) RadioButton rCompleted;
    @BindView(R.id.r_drop) RadioButton rDrop;
    @BindView(R.id.r_plan) RadioButton rPlan;

    private CourseEditorViewModel mViewModel;
    private SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
    private boolean mNewCourse, mEditing;
    private Context myContext;
    private AlarmManager mgrAlarms[];
    private ArrayList<PendingIntent> intentArray;
    private Date dStart = new Date();
    private Date dEnd = new Date();
    private int statusInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);


        ButterKnife.bind(this);
        mViewNote.setVisibility(View.GONE);
        mViewNoteText.setVisibility(View.GONE);
        mViewAssess.setVisibility(View.GONE);
        mViewAssText.setVisibility(View.GONE);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        mViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(CourseEntity courseEntity) {
                if (courseEntity != null && !mEditing) {
                    System.out.println(courseEntity.getTermId());
                    currentCourse=courseEntity.getId();
                    cCourse=courseEntity;
                    mName.setText(courseEntity.getName());
                    mStart.setText(TermViewActivity.shortTime(courseEntity.getStart()));
                    mEnd.setText(TermViewActivity.shortTime(courseEntity.getEnd()));
                    mMentor.setText(courseEntity.getMentor());
                    mPhone.setText(courseEntity.getPhone());
                    mEmail.setText(courseEntity.getEmail());
                    if(courseEntity.getStatus()==0){
                        rProgress.toggle();
                    } else if(courseEntity.getStatus()==1){
                        rCompleted.toggle();
                    } else if(courseEntity.getStatus()==2){
                        rDrop.toggle();
                    } else if(courseEntity.getStatus()==3){
                        rPlan.toggle();
                    }
                }
            }
        });



        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New course");
            mNewCourse = true;
        } else {
            setTitle("Edit course");
            int courseId = extras.getInt(COURSE_ID_KEY);
            mViewModel.loadData(courseId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_course_editor, menu);
            mViewNoteText.setVisibility(View.VISIBLE);
            mViewNote.setVisibility(View.VISIBLE);
            mViewAssess.setVisibility(View.VISIBLE);
            mViewAssText.setVisibility(View.VISIBLE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        currentCourse=0;
        cCourse=null;
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            mViewModel.deleteCourse();
        } else if (item.getItemId() == R.id.action_enable_alarms) {
            triggerAlarms();
        }
        finish();
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        currentCourse=0;
        cCourse=null;
        saveAndReturn();
    }

    private void saveAndReturn() {
        if(rProgress.isChecked()){
            statusInput=0;
        }else if(rCompleted.isChecked()){
            statusInput=1;
        }else if(rDrop.isChecked()){
            statusInput=2;
        }else if(rPlan.isChecked()){
            statusInput=3;
        }
        try {
            dStart = formatter.parse(mStart.getText().toString());
            dEnd = formatter.parse(mEnd.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mViewModel.saveCourse(Constants.currentTerm,statusInput,mName.getText().toString(),dStart,dEnd,
                mMentor.getText().toString(),mPhone.getText().toString(),mEmail.getText().toString());
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    public void viewNotes(View view){
        Intent intent = new Intent(this, NoteViewer.class);
        startActivity(intent);
    }

    public void viewTests(View view){
        Intent intent = new Intent(this, TestViewer.class);
        startActivity(intent);
    }

    public void triggerAlarms(){
        //Trigger test alarms
        myContext = getApplicationContext();
        mgrAlarms = new AlarmManager[2];
        intentArray = new ArrayList<PendingIntent>();
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();
        try {
            dStart = formatter.parse(mStart.getText().toString());
            dEnd = formatter.parse(mEnd.getText().toString());
            cStart.setTime(dStart);
            cEnd.setTime(dEnd);
            cNow.setTime(new Date());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intents[] = new Intent[2];
        for(int i = 0; i < 2; ++i)
        {
            if(i==0){
                intents[i] = new Intent(myContext, AlertReceiver.class);
                // Loop counter `i` is used as a `requestCode`
                PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext,
                        Constants.intentArray.size()+200, intents[i], 0);
                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                mgrAlarms[i] = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);
                mgrAlarms[i].setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        cStart.getTimeInMillis()-cNow.getTimeInMillis(),
                        pendingIntent);
                Constants.intentArray.add(pendingIntent);
            } else if(i==1){
                intents[i] = new Intent(myContext, AlertReceiver.class);
                // Loop counter `i` is used as a `requestCode`
                PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext,
                        Constants.intentArray.size()+200, intents[i], 0);
                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                mgrAlarms[i] = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);
                mgrAlarms[i].setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        cEnd.getTimeInMillis()-cNow.getTimeInMillis(),
                        pendingIntent);
                Constants.intentArray.add(pendingIntent);
            }
        }
        //
        System.out.println("Alarm start set for:" + dStart);
        System.out.println("Alarm end set for:" + dEnd);
        saveAndReturn();
    }
}
