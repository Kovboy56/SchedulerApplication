package com.example.schoolscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.utilities.AlertReceiver;
import com.example.schoolscheduler.utilities.AlertReceiverTest;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.viewmodel.TestEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.schoolscheduler.utilities.Constants.COURSE_ID_KEY;
import static com.example.schoolscheduler.utilities.Constants.EDITING_KEY;
import static com.example.schoolscheduler.utilities.Constants.TEST_ID_KEY;

public class TestEditorActivity extends AppCompatActivity {

    @BindView(R.id.code_input) TextView mCode;
    @BindView(R.id.name_input) TextView mName;
    @BindView(R.id.date_input) TextView mDate;
    @BindView(R.id.desc_input) TextView mDesc;
    private SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");

    private TestEditorViewModel mViewModel;
    private boolean mNewTest, mEditing;
    private ArrayList<PendingIntent> intentArray;
    private AlarmManager mgrAlarms[];
    private Context myContext;
    private Date dStart = new Date();
    private Calendar cStart = Calendar.getInstance();
    private Calendar cNow = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(TestEditorViewModel.class);

        mViewModel.mLiveTest.observe(this, new Observer<TestEntity>() {
            @Override
            public void onChanged(TestEntity testEntity) {
                if (testEntity != null && !mEditing) {
                    System.out.println(testEntity.getCourseId());
                    mCode.setText(testEntity.getCode());
                    mName.setText(testEntity.getName());
                    mDate.setText(testEntity.getStartDate().toString());
                    mDesc.setText(testEntity.getDescription());
                }
            }
        });



        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New test");
            mNewTest = true;
        } else {
            setTitle("Edit test");
            int testId = extras.getInt(TEST_ID_KEY);
            mViewModel.loadData(testId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewTest) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_test_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            mViewModel.deleteTest();
        } else if (item.getItemId() == R.id.action_enable_alarms) {
            triggerAlarm();
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        saveAndReturn();
    }

    private void saveAndReturn() {
        Date dStart = new Date();
        try {
            dStart = formatter.parse(mDate.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mViewModel.saveTest(Constants.currentCourse,dStart,mCode.getText().toString(),mName.getText().toString(),mDesc.getText().toString());
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    public void triggerAlarm(){
        //Trigger test alarms
        myContext = getApplicationContext();
        mgrAlarms = new AlarmManager[1];
        intentArray = new ArrayList<PendingIntent>();
        cStart = Calendar.getInstance();
        cNow = Calendar.getInstance();
        try {
            dStart = formatter.parse(mDate.getText().toString());
            cStart.setTime(dStart);
            cNow.setTime(new Date());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intents[] = new Intent[1];
        for(int i = 0; i < 1; ++i)
        {
            intents[i] = new Intent(myContext, AlertReceiverTest.class);
            // Loop counter `i` is used as a `requestCode`
            PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext,
                    Constants.intentArray.size(), intents[i], 0);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
            mgrAlarms[i] = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);
            mgrAlarms[i].setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    cStart.getTimeInMillis()-cNow.getTimeInMillis(),
                    pendingIntent);
            Constants.intentArray.add(pendingIntent);
        }
        //
        saveAndReturn();
    }
}
