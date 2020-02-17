package com.example.schoolscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.TermEntity;
import com.example.schoolscheduler.ui.TermAdapter;
import com.example.schoolscheduler.utilities.AlertReceiver;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.utilities.NotificationHelper;
import com.example.schoolscheduler.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermViewer extends AppCompatActivity {

    @BindView(R.id.term_list)
    RecyclerView mTermView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, TermViewActivity.class);
        startActivity(intent);
    }

    private List<TermEntity> termsData = new ArrayList<>();
    private TermAdapter mAdapter;
    private MainViewModel mViewModel;
    private NotificationHelper mNotificationHelper;
    private Context myContext;
    private AlarmManager mgrAlarms[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNotificationHelper = new NotificationHelper(this);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
        setTitle("View of terms");

    }

    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver =
                new Observer<List<TermEntity>>() {
                    @Override
                    public void onChanged(List<TermEntity> termEntities) {
                        termsData.clear();
                        termsData.addAll(termEntities);

                        if (mAdapter == null) {
                            mAdapter = new TermAdapter(termsData,
                                    TermViewer.this);
                            mTermView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mViewModel.mTerms.observe(this, termsObserver);
    }

    private void initRecyclerView() {
        mTermView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTermView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                mTermView.getContext(), layoutManager.getOrientation());
        mTermView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllTerms();
            return true;
        } else if (id == R.id.action_add_test_alarms) {
            triggerTestAlarms();
        }

        return super.onOptionsItemSelected(item);
    }

    public void triggerTestAlarms() {
        //Trigger test alarms
        myContext = getApplicationContext();
        mgrAlarms = new AlarmManager[10];

        Intent intents[] = new Intent[10];
        for(int i = 1; i < 10; ++i)
        {
            System.out.println(Constants.intentArray.size());
            intents[i] = new Intent(myContext, AlertReceiver.class);
            // Loop counter `i` is used as a `requestCode`
            PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext,
                    Constants.intentArray.size()+500, intents[i], 0);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
            mgrAlarms[i] = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);
            mgrAlarms[i].setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + i*5000,
                    pendingIntent);
            Constants.intentArray.add(pendingIntent);
        }
        //
    }

    private void deleteAllTerms() {
        mViewModel.deleteAllTerms();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }

    private void addTestAlarms(){
        Calendar c = Calendar.getInstance();
        onTimeSet(c.HOUR_OF_DAY,c.MINUTE+1, 100);
        onTimeSet(c.HOUR_OF_DAY,c.MINUTE+2, 101);
    }

    public void openNext(View view) {
        Intent intent = new Intent(this, TermViewActivity.class);
        startActivity(intent);
    }

    public void sendOnChannel1(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title,message);
        mNotificationHelper.getManager().notify(1,nb.build());
    }

    public void sendOnChannel2(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannel2Notification(title,message);
        mNotificationHelper.getManager().notify(2,nb.build());
    }

    public void onTimeSet(int hour, int min, int rc){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);

        startAlarm(c,rc);
    }

    private void startAlarm(Calendar c, int rc) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rc, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

}
